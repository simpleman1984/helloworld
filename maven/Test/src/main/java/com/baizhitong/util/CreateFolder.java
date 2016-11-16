package com.baizhitong.util;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * @goal create
 * @phase process-sources
 */
public class CreateFolder extends AbstractMojo {

	/**
     * 获取源代码路径
     * @parameter property="project.build.sourceDirectory"
     * @required
     */
    private File srcDirectory;
    
    /**
     * 需要生成的package路径
     * @parameter property="package"
     * @required
     */
    private String packageName;
    
	public void execute() throws MojoExecutionException, MojoFailureException {
		try{
			mkdirs("dao");
		}catch(Exception e)
		{
			getLog().error("生成:" + packageName + " 目录异常", e);
		}
	}

	/**
	 * 创建目录
	 * @param folder
	 */
	private void mkdirs(String folder){
		//生成dao目录
		File newFolder = new File(srcDirectory,packageName + File.separator + folder);
		newFolder.mkdir();
	}
}
