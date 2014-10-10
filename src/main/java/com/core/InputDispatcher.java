package com.core;

import org.springframework.beans.factory.InitializingBean;
import reactor.event.Event;
import reactor.function.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys Kovalenko on 10/8/2014.
 */
public class InputDispatcher implements Consumer<Event<String>>, InitializingBean {
    private List<FlowSpecification> flowSpecifications;

    public void registerFlow(FlowSpecification flowSpecification){
        flowSpecifications.add(flowSpecification);
    }

    @Override
    public void accept(Event<String> event) {
        for(FlowSpecification flow : flowSpecifications){
            flow.process(event.getData());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        flowSpecifications = new ArrayList<FlowSpecification>();
    }
}
