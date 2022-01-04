# maven 加密插件
># 一、目的
- 能够实现一键操作实现打包过程；
- 设置参数实现不同的处理逻辑

># 二、快速使用

>## 1、创建动态代理库
>######项目路径下 encrypt.c 文件是 C++ 源代码，通过以下方式编译成相应的本地动态库文件，并将生成的动态库文件添加到系统路径
> - windows
>>     gcc -shared encrypt.c -o encrypt.dll -I "{JAVA_HOME}/include" -I "{JAVA_HOME}/include/win32"
> - linux
>>     gcc -shared encrypt.c -o encypt.so -I "{JAVA_HOME}/include" -I "{JAVA_HOME}/include/linux"
>
> Java 可通过以下方式查看系统路径
>    
>     System.getProperty("java.library.path")
>### 2、配置 pom.xml 文件
>#### 2.1、引入插件依赖
    <project>
        <dependencies>
            <!-- 加密 Maven 插件依赖-->
            <dependency>
                <groupId>com.wyhw.plugin</groupId>
                <artifactId>encrypt-maven-plugin</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </dependency>
        </dependencies>
    </project>

>#### 2.2、配置插件
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
     
            <!-- 加密插件 -->
            <plugin>
                <groupId>com.wyhw.plugin</groupId>
                <artifactId>encrypt-maven-plugin</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>encrypt</goal>
                        </goals>
                        <!-- configuration 配置可忽略（未配置时使用系统默认值） -->
                        <configuration>
                            <!-- 是否加密，默认为 true -->
                            <on>true</on>
                            <!-- 加密包名后缀，默认为 “-encrypt” -->
                            <archiveSuffix>-encrypt</archiveSuffix>
                            <!-- 未实现 -->
                            <!-- 是否覆盖原 jar 包，默认为 false -->
                            <over>false</over>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
> Tip：参数可通过命令行参数设置<br>
    `com.wyhw.plugins.plugin.encrypt.EncryptMojo`
   
    /**
     * 是否执行加密逻辑标识，默认“不加密”
     * 支持命令行设置，如：mvn package -Dencrypt.on=true
     */
    @Parameter(property = "encrypt.on", defaultValue = "true")
    private boolean on;
   
    /**
     * 是否覆盖原包，默认“不覆盖”
     * 支持命令行设置，如：mvn package -Dencrypt.over=true
     */
    @Parameter(property = "encrypt.over", defaultValue = "false")
    private boolean over;
   
    /**
     * 生成新包的名称后缀，例如：encrypt-maven-plugin-0.0.1-SNAPSHOT-entry.jar
     * 支持命令行设置，如：mvn package -Dencrypt.suffix=-entry
     */
    @Parameter(property = "encrypt.suffix", defaultValue = "-entry")
    private String archiveSuffix;

>#### 2.4、打加密包
>###### 自定义加密插件的Maven 生命周期定义为 package，经过配置以上信息，表示会将 spring-boot-maven-plugin 插件生成的可执行 Jar 包进行加密处理，生成后缀为 “-encrypt” 的可执行加密 Jar 包`
    
    mvn clean package

>###### 经过以上配置，可以生成加密 Jar 包，此时通过 java -jar 命令直接执行 jar 包时，jar 包运行失败，因为此时的 class 文件还是处于加密状态，jvm 加载 class 文件时校验失败；

    java -jar your-executeable-jar-file.jar

>###### 因此，需要使用 -agentlib 参数，指定自定义的动态链接库（encrypt.dll - windows/encrypt.so - linux），完成 class 文件的解密处理
    
    java -agentlib:encrypt -jar your-executeable-jar-file.jar