package com.eappcat.autogpt;

import com.alibaba.fastjson.JSONObject;
import com.eappcat.autogpt.command.Command;
import com.eappcat.autogpt.command.CommandResult;
import com.eappcat.autogpt.command.ExecuteContext;
import com.eappcat.autogpt.command.impl.DoNothingCommand;
import com.eappcat.autogpt.command.impl.GoogleCommand;
import com.eappcat.autogpt.command.impl.TaskCompleteCommand;
import com.eappcat.autogpt.exception.JsonParseException;
import com.eappcat.autogpt.models.template.*;
import com.eappcat.autogpt.models.thought.ThoughtResponse;
import com.eappcat.autogpt.service.IMemoryService;
import com.eappcat.autogpt.service.OpenAiService;
import com.eappcat.autogpt.enums.PromptTemplateLang;
import com.eappcat.autogpt.service.PromptTemplateService;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class AutogptApplicationTests {

	@Autowired
	private PromptTemplateService promptTemplateService;

	@Autowired
	private OpenAiService openAiService;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private IMemoryService memoryService;

	@Test
	void testGoogleCommand() throws Exception{
		GoogleCommand command = new GoogleCommand();
		HashMap<String,String> params = new HashMap<>();
		params.put("query","iPhone14 in US market");
		ExecuteContext executeContext = new ExecuteContext();
		executeContext.setArgs(params);
		CommandResult result = command.execute(executeContext);
		log.info("result: {}",result);
	}

	@Test
	void main() throws Exception {
		PromptTemplateLang templateLang = PromptTemplateLang.CN;

		MainPrompt prompt = new MainPrompt();
		prompt.setAiName("AppleAI");
		prompt.setRole("你是一个数据分析师，将使用互联网上的信息分析内容。");
		prompt.setGoals(Lists.newArrayList("找出iPhone14在中国市场的维修率，并归类前三位原因，利用爬虫软件抓取现有数据，并列出爬虫抓取数据的数据源地址."));
		String lang = promptTemplateService.parse(prompt, templateLang);
		log.info("{}",lang);
		String taskId="1";


		String date = promptTemplateService.parse(new CurrentDatePrompt(),templateLang);

		String nextStep = promptTemplateService.parse(new NextCommandPrompt(),templateLang);


		int stepCount = 0;
		int maxStepCount = 5;

		List<String> shortMemory = new ArrayList<>();

		while (true){
			stepCount++;
			log.info("任务开始执行，{}/{}", stepCount,maxStepCount);
			if (stepCount>maxStepCount){
				log.info("任务执行达到最大执行次数，任务自动停止");
			}
			List<String> memories = new ArrayList<>();
			if (shortMemory.size()>0){
				// 查找相似度比较类似的内容
				memories = memoryService.getMemoryBySize(taskId,shortMemory.get(shortMemory.size()-1),3);
			}

			String memory = promptTemplateService.parse(new MemoryPrompt(memories),templateLang);

			List<Message> messages = new ArrayList<>();
			messages.add(Message.builder().role(Message.Role.SYSTEM).content(lang).build());
			messages.add(Message.builder().role(Message.Role.SYSTEM).content(date).build());
			messages.add(Message.builder().role(Message.Role.SYSTEM).content(memory).build());
			messages.add(Message.builder().role(Message.Role.USER).content(nextStep).build());

			String messageText = openAiService.chat(messages ,null);
			ThoughtResponse thoughtResponse = null;

			try {
				String message = StringUtils.substringAfter(messageText,"{");
				message = StringUtils.substringBeforeLast(message,"}");
				message = "{" + message+ "}";
				thoughtResponse = JSONObject.toJavaObject(JSONObject.parseObject(message), ThoughtResponse.class);

			}catch (Exception e){
				log.error("error to parse data to json");
				throw new JsonParseException("Failed to parse json for data: "+ messageText);
			}

			if (thoughtResponse!=null&&thoughtResponse.getCommand()!=null&&thoughtResponse.getThoughts()!=null){
				ThoughtResponse.Thoughts thoughts = thoughtResponse.getThoughts();
				log.info("我的思考内容：\n{}", thoughts);

				log.info("我将要执行的内容：\n{}",thoughtResponse.getCommand());

				ExecuteContext executeContext = new ExecuteContext();
				executeContext.setTaskId(taskId);
				executeContext.setLang(templateLang);

				Map<String,Command> beans =  applicationContext.getBeansOfType(Command.class);
				CommandResult reuslt = null;
				for (String  key: beans.keySet()) {
					Command cmd = beans.get(key);
					if (cmd.accept(thoughtResponse.getCommand().getName())){
						log.info("触发对应的执行器：{}, 参数为：",cmd.getClass(), thoughtResponse.getCommand().getArgs());
						executeContext.setArgs(thoughtResponse.getCommand().getArgs());
						reuslt = cmd.execute(executeContext);
						break;
					}
				}
				if (reuslt == null){
					log.info("未找到指定的命令");
					memoryService.addMemory(taskId,"未能找到指定命令。");
					return;
				}else {
					log.info("命令响应成功:\n {}",reuslt);
				}

				if (reuslt.getCommand() instanceof DoNothingCommand){
					continue;
				}
				if (reuslt.getCommand() instanceof TaskCompleteCommand){
					break;
				}

				CommandResultPrompt commandResultPrompt = new CommandResultPrompt(thoughtResponse.getCommand().getName(),reuslt.getData());
				String cmdResult = promptTemplateService.parse(commandResultPrompt,templateLang);

				MemoryStorePrompt memoryStorePrompt = new MemoryStorePrompt(messageText,cmdResult);
				String memoryText = promptTemplateService.parse(memoryStorePrompt,templateLang);
				memoryService.addMemory(taskId,memoryText);
				shortMemory.add(memoryText);

				log.info("{}",messageText);

				log.info("我将思考下一步行动");
			}

		}







	}



}
