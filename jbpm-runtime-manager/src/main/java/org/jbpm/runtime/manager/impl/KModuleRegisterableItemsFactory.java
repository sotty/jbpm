package org.jbpm.runtime.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.compiler.kie.util.CDIHelper;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.WorkingMemoryEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.WorkItemHandler;

/**
 * This implementation extends DefaultRegisterableItemsFactory
 * and relies on definitions of work item handlers and
 * listeners that come from kmodule.xml from kjar. 
 * It will directly register all stuff on ksession and will return
 * listeners and handlers provided by default implementation.
 *
 */
public class KModuleRegisterableItemsFactory extends DefaultRegisterableItemsFactory {

    private KieContainer kieContainer;
    private String ksessionName;
    
    public KModuleRegisterableItemsFactory(KieContainer kieContainer,
            String ksessionName) {
        super();
        this.kieContainer = kieContainer;
        this.ksessionName = ksessionName;
    }

    @Override
    public Map<String, WorkItemHandler> getWorkItemHandlers(RuntimeEngine runtime) {
        KieSessionModel ksessionModel = null;
        if(ksessionName != null) {
            ksessionModel = ((KieContainerImpl)kieContainer).getKieSessionModel(ksessionName);
        }
        else {
            ksessionModel = ((KieContainerImpl)kieContainer).getKieProject().getDefaultKieSession();
        }
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("ksession", runtime.getKieSession());
        parameters.put("taskService", runtime.getKieSession());
        parameters.put("runtimeManager", ((RuntimeEngineImpl)runtime).getManager());
        CDIHelper.wireListnersAndWIHs(ksessionModel, runtime.getKieSession(), parameters);
        
        return super.getWorkItemHandlers(runtime);
    }

    @Override
    public List<ProcessEventListener> getProcessEventListeners(RuntimeEngine runtime) {
        return super.getProcessEventListeners(runtime);
    }

    @Override
    public List<AgendaEventListener> getAgendaEventListeners(RuntimeEngine runtime) {
        return super.getAgendaEventListeners(runtime);
    }

    @Override
    public List<WorkingMemoryEventListener> getWorkingMemoryEventListeners(
            RuntimeEngine runtime) {
        return super.getWorkingMemoryEventListeners(runtime);
    }

}
