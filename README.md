# 目的
    1、能够实现一键操作实现打包过程；
    2、设置参数实现不同的处理逻辑
# 快速使用
1、引入 pom.xml 依赖

    <dependency>
        <groupId>com.zk.plugin</groupId>
        <artifactId>encrypt-maven-plugin</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    
2、配置 pom.xml 插件

    <plugin>
        <groupId>com.zk.plugin</groupId>
        <artifactId>encrypt-maven-plugin</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <executions>
            <execution>
                <goals>
                    <goal>encrypt</goal>
                </goals>
                <configuration>										<!-- 可选参数，未配置参数时，使用默认参数 -->
                    <on>true</on>									<!-- 是否加密，默认为 true -->
                    <archiveSuffix>-encrypt</archiveSuffix>			<!-- 生成的加密包后缀，默认为“-encrype” -->
                    <over>false</over>								<!-- 是否覆盖原Jar文件，默认为 false --><!-- 暂未实现 -->
                </configuration>
            </execution>
        </executions>
    </plugin>

3、执行命令

    mvn clean package

此时会在 springboot jar 包同级目录下生成后缀名为“-encrypt.jar” 的加密 jar 包，至此完成 jar 包加密过程