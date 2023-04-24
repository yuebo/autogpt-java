# AutoGPT for java
----------------------------------

## 简述
本仓库根据AutoGPT Python版本的实现原理，用java语言进行实现，支持Prompt的多语言切换。

## 参考内容
1. https://github.com/Grt1228/chatgpt-java
2. https://github.com/Significant-Gravitas/Auto-GPT

## 配置内容
目前只是测试版本，故逻辑写在测试类里面，需要配置以下环境变量(OPENAI_API_KEY)才可以使用。

本版本目前还有一些问题，待后续解决。



## 实现的指令
* append_to_file
* delete_file
* read_file
* search_files
* google_search
* browse_website
* task_complete

## 带实现功能

* 自动计算请求的token，防止token超过长度
* 根据命令内容动态加载命令
* 增加执行脚本指令(groovy)

