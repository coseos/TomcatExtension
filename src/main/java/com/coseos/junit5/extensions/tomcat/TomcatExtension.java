/** TomcatExtension
 * 
 * A JUnit5 extension to run the Tomcat web server and register a servlet
 *  
 * Copyright (c) 2019 by Thorsten J. Lorenz / coseos
 *  
 * Read and respect the license as documented in the /LICENSE file supplied with
 * this code. Distribution of this file without the LICENSE file is in conflict
 * with the license of this software.
 *  
 * Please send reports about a license violation to license-violation@coseos.com 
 * 
 */
package com.coseos.junit5.extensions.tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**  TomcatExtension
 * 
 * Usage:
 *
 * In your unit test class, add the extension as a static field with:
 * 
 * 	  @RegisterExtension
 *    static TomcatExtension tomcatExtension = TomcatExtension.builder()
 *        .host("localhost")
 *        .port(8070)
 *        .clazz(ReverseProxyServlet.class)
 *        .build();
 * 
 * You may also provide values for AppBase, DocBase, Context and Mappings.
 * 
 * Note:
 * 
 * "tomcat.getServer().await();" will block and wait for tomcat shutdown.
 *
 * @author tlorenz
 */
public class TomcatExtension implements BeforeAllCallback, AfterAllCallback {

	private Tomcat tomcat;
	private final Class<?> clazz;
	private final String host;
	private final int port;
	private final String appBase;
	private final String docBase;
	private final String context;
	private final String mapping;
	private final int wait;
	
	private TomcatExtension(final Class<?> clazz, final String host, final int port, final String appBase, final String docBase, final String context, final String mapping, final int wait) {
		this.clazz = clazz;
		this.host = host;
		this.port = port;
		this.appBase = appBase;
		this.docBase = docBase;
		this.context = context;
		this.mapping = mapping;
		this.wait = wait;
	}

	@Override
	public void afterAll(ExtensionContext notUsed) throws Exception {
		tomcat.stop();
	}

	@Override
	public void beforeAll(ExtensionContext notUsed) throws Exception {
		tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.setHostname(host);
		tomcat.getHost().setAppBase(appBase);
		
		File docBase = new File(this.docBase);
		Context context = tomcat.addContext(this.context, docBase.getAbsolutePath());
		Tomcat.addServlet(context, clazz.getSimpleName(),clazz.getName());
		context.addServletMappingDecoded(mapping, clazz.getSimpleName());

		tomcat.start();
		Thread.sleep(wait);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {

		private String host = "localhost";
		private Class<?> clazz = DefaultServlet.class;
		private int port = 8086;
		private int wait = 1000;
		private String appBase = ".";
		private String docBase = ".";
		private String context = "";
		private String mapping = "/";

		public Builder clazz(final Class<?> clazz) {
			this.clazz = clazz;
			return this;
		}
		
		public Builder appBase(final String appBase) {
			this.appBase = appBase;
			return this;
		}

		public Builder docBase(final String docBase) {
			this.docBase = docBase;
			return this;
		}

		public Builder context(final String context) {
			this.context = context;
			return this;
		}

		public Builder mapping(final String mapping) {
			this.mapping = mapping;
			return this;
		}
		
		public Builder host(final String host) {
			this.host= host;
			return this;
		}
		
		public Builder port(final int port) {
			this.port= port;
			return this;
		}
		
	    public Builder wait(final int wait) {
		    this.wait = wait;
		    return this;
	    }
		
		public TomcatExtension build() {
			return new TomcatExtension(clazz,host,port,appBase,docBase,context,mapping,wait);
		}
	}
}
