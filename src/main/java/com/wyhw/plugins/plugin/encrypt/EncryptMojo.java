package com.wyhw.plugins.plugin.encrypt;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * maven 加密插件（只适用于 springboot 项目）
 *
 * 此插件处于 Maven 生命周期的 package 阶段（若多个插件同处于 package 阶段时，此插件应处于最后执行的位置），
 * 将 spring-boot-maven-plugin 插件生成的可执行 jar 包加密处理，生成加密包
 *
 * @author wanyanhw
 * @since 2021/12/31 10:44
 */
@Mojo(
		name = "encrypt",
		defaultPhase = LifecyclePhase.PACKAGE,
		requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class EncryptMojo extends AbstractMojo{

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

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

	public void setOn(String on) {
		this.on = Boolean.valueOf(on);
	}

	public void setOver(String over) {
		this.over = Boolean.valueOf(over);
	}

	public void setArchiveSuffix(String archiveSuffix) {
		this.archiveSuffix = archiveSuffix;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		Log log = getLog();
		if (!on) {
			log.info("Disable encrypt package !!!");
			return;
		}
		String originalArchivePath = project.getArtifact().getFile().getAbsolutePath();
		log.info("Original file path: " + originalArchivePath);
		int lastSeparatorIndex = originalArchivePath.lastIndexOf(".");
		String fileName = originalArchivePath.substring(0, lastSeparatorIndex);
		String extendName = originalArchivePath.substring(lastSeparatorIndex + 1);
		String newFileName = fileName + archiveSuffix + "." + extendName;
		log.info("Encrypt file path: " + newFileName);
		try {
			if (over) {
				log.info("Overwrite original file");
				newFileName = originalArchivePath;
			}
			JarEncryptUtil.encrypt(originalArchivePath, newFileName);
		} catch (Exception e) {
			log.error("自动加密失败", e);
		}
	}
}
