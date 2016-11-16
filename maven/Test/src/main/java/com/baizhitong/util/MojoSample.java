package com.baizhitong.util;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * 
 * @phase process-sources
 * 
 * Maven内置变量说明：
	${basedir} 项目根目录
	${project.build.directory} 构建目录，缺省为target
	${project.build.outputDirectory} 构建过程输出目录，缺省为target/classes
	${project.build.finalName} 产出物名称，缺省为${project.artifactId}-${project.version}
	${project.packaging} 打包类型，缺省为jar
	${project.xxx} 当前pom文件的任意节点的内容
 */
public class MojoSample
    extends AbstractMojo
{
	/**
     * The greeting to display.
     * @parameter property="greeting"
     */
    private String greeting;
    
    /**
     * Location of the file.
     * @parameter property="project.source.directory"
     */
    private String sourceDirectory;
    
    /**
     * Location of the file.
     * @parameter property="basedir"
     * @required
     */
    private File baseDirectory;
    
    /**
     * Location of the file.
     * @parameter property="project.build.sourceDirectory"
     * @required
     */
    private String srcDirectory;
    
    /**
     * Location of the file.
     * @parameter property="project.build.directory"
     * @required
     */
    private File outputDirectory;

    public void execute()
        throws MojoExecutionException
    {
//    	System.out.println(this.getPluginContext());
    	
        File f = outputDirectory;
        
    	getLog().info("Hello world :" + greeting + ",输出目录:" + outputDirectory.getAbsolutePath() + ",源代码目录:" + sourceDirectory + ",baseDirectory:" + baseDirectory.getAbsolutePath()+",srcDirectory:" + srcDirectory);  
    	
        if ( !f.exists() )
        {
            f.mkdirs();
        }

        File touch = new File( f, "touch.txt" );

        FileWriter w = null;
        try
        {
            w = new FileWriter( touch );

            w.write( "touch.txt" );
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Error creating file " + touch, e );
        }
        finally
        {
            if ( w != null )
            {
                try
                {
                    w.close();
                }
                catch ( IOException e )
                {
                    // ignore
                }
            }
        }
    }
    
}
