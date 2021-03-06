package org.jbpm.process.workitem.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.drools.compiler.compiler.BPMN2ProcessFactory;
import org.drools.compiler.compiler.ProcessBuilderFactory;
import org.drools.core.impl.EnvironmentFactory;
import org.drools.core.marshalling.impl.ProcessMarshallerFactory;
import org.drools.core.runtime.process.ProcessRuntimeFactory;
import org.jbpm.bpmn2.BPMN2ProcessProviderImpl;
import org.jbpm.marshalling.impl.ProcessMarshallerFactoryServiceImpl;
import org.jbpm.process.builder.ProcessBuilderFactoryServiceImpl;
import org.jbpm.process.instance.ProcessRuntimeFactoryServiceImpl;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSessionConfiguration;

/**
 * This is a sample file to launch a process.
 */
public class HandlerTest extends TestCase {

	public void testHandler() throws Exception {
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = createSession(kbase);
		ksession.getWorkItemManager().registerWorkItemHandler("Handler", new JavaHandlerWorkItemHandler(ksession));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("employeeId", "12345-ABC");
		ksession.startProcess("com.sample.bpmn.java", params);
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		ProcessBuilderFactory.setProcessBuilderFactoryService(new ProcessBuilderFactoryServiceImpl());
		ProcessMarshallerFactory.setProcessMarshallerFactoryService(new ProcessMarshallerFactoryServiceImpl());
		ProcessRuntimeFactory.setProcessRuntimeFactoryService(new ProcessRuntimeFactoryServiceImpl());
		BPMN2ProcessFactory.setBPMN2ProcessProvider(new BPMN2ProcessProviderImpl());
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("JavaHandler.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();
	}
	
	private static StatefulKnowledgeSession createSession(KnowledgeBase kbase) {
		Properties properties = new Properties();
		properties.put("drools.processInstanceManagerFactory", "org.jbpm.process.instance.impl.DefaultProcessInstanceManagerFactory");
		properties.put("drools.processSignalManagerFactory", "org.jbpm.process.instance.event.DefaultSignalManagerFactory");
		KieSessionConfiguration config = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(properties);
		return kbase.newStatefulKnowledgeSession(config, EnvironmentFactory.newEnvironment());
	}
}
