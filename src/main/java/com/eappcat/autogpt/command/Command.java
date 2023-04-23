package com.eappcat.autogpt.command;

/**
 * Command interface,
 * Each command should extend this.
 * @param <T>
 */
public interface Command {
    /**
     * 执行命令
     * @param context 命令上下文
     * @return
     * @throws Exception
     */
    CommandResult execute(ExecuteContext context) throws Exception;

    /**
     * 是否执行命令
     * @param cmd
     * @return
     */
    default boolean accept(String cmd) { return false; };

    /**
     * 命令描述
     * @return
     */
    default String description() { return ""; };
}
