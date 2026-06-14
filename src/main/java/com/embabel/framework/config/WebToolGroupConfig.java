package com.embabel.framework.config;

import com.embabel.agent.core.ToolGroup;
import com.embabel.agent.core.ToolGroupDescription;
import com.embabel.agent.core.ToolGroupPermission;
import com.embabel.agent.tools.mcp.McpToolGroup;
import com.embabel.agent.tools.mcp.ToolCallContextMcpMetaConverter;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class WebToolGroupConfig {

    @Bean
    public ToolGroup mcpWebToolsGroup(List<McpSyncClient> mcpSyncClients) {
        return new McpToolGroup(
                ToolGroupDescription.create("Web search and fetch tools", "web"),
                "Brave",
                "brave-web",
                Set.of(ToolGroupPermission.INTERNET_ACCESS),
                mcpSyncClients,
                tool -> true,
                ToolCallContextMcpMetaConverter.passThrough()
        );
    }
}