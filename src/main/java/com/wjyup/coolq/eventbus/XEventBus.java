package com.wjyup.coolq.eventbus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.eventbus.Subscribe;
import com.wjyup.coolq.exception.UnexpectedException;
import com.wjyup.coolq.util.Predicates;
import com.wjyup.coolq.util.ScanPackage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class XEventBus implements InitializingBean {
    private Logger log = LogManager.getLogger(XEventBus.class);

    static class MethodInfo {
        private Object instance;
        private Method method;

        public MethodInfo(Object instance, Method method) {
            super();
            this.instance = instance;
            this.method = method;
        }

        public Object getInstance() {
            return instance;
        }

        public void setInstance(Object instance) {
            this.instance = instance;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public void invoke(Object arg) throws Throwable {
            try {
                if(this.method.getParameterCount() == 1) {
                    this.method.invoke(instance, arg);
                } else {
                    this.method.invoke(instance, arg, null);
                }
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new UnexpectedException(e);
            }
        }

        public void invoke(Object arg1, Object arg2) throws Throwable {
            try {
                this.method.invoke(instance, arg1, arg2);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw new UnexpectedException(e);
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((instance == null) ? 0 : instance.hashCode());
            result = prime * result + ((method == null) ? 0 : method.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MethodInfo other = (MethodInfo) obj;
            if (instance == null) {
                if (other.instance != null)
                    return false;
            } else if (!instance.equals(other.instance))
                return false;
            if (method == null) {
                if (other.method != null)
                    return false;
            } else if (!method.equals(other.method))
                return false;
            return true;
        }
    }

    public static class EventId {
        private Class<? extends XEvent> eventClass;
        private Class<?> eventArgClass;

        public static EventId of(Class<? extends XEvent> eventClass) {
            EventId eventId = new EventId();
            eventId.setEventClass(eventClass);
            return eventId;
        }

        public static EventId of(Class<? extends XEvent> eventClass, Class<?> eventArgClass) {
            EventId eventId = new EventId();
            eventId.setEventClass(eventClass);
            eventId.setEventArgClass(eventArgClass);
            return eventId;
        }

        public String name() {
            if(eventArgClass == null) {
                return eventClass.getName();
            } else {
                return String.format("%s(%s)", eventClass.getName(), eventArgClass.getName());
            }
        }

        public Class<? extends XEvent> getEventClass() {
            return eventClass;
        }

        public void setEventClass(Class<? extends XEvent> eventClass) {
            this.eventClass = eventClass;
        }

        public Class<?> getEventArgClass() {
            return eventArgClass;
        }

        public void setEventArgClass(Class<?> eventArgClass) {
            this.eventArgClass = eventArgClass;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((eventArgClass == null) ? 0 : eventArgClass.getName().hashCode());
            result = prime * result + ((eventClass == null) ? 0 : eventClass.getName().hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EventId other = (EventId) obj;
            if (eventArgClass == null) {
                if (other.eventArgClass != null)
                    return false;
            } else if (!eventArgClass.equals(other.eventArgClass))
                return false;
            if (eventClass == null) {
                if (other.eventClass != null)
                    return false;
            } else if (!eventClass.equals(other.eventClass))
                return false;
            return true;
        }
    }

    private List<EventId> eventIdList = new ArrayList<>();
    private boolean trace = true;
    private ThreadLocal<Stack<String>> eventStack = new ThreadLocal<>();
    private IdentityHashMap<Object, Object> targetSet = new IdentityHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

        final String[] excludes = { "java.", "javax." };
        Set<Class<?>> classes = ScanPackage.getClasses("com.wjyup.coolq");
        classes.forEach(clazz -> {
            if (clazz != null) {
                String packageName = clazz.getPackage().getName();
                for (String prefix : excludes) {
                    if (packageName.startsWith(prefix)) {
                        return;
                    }
                }

                if(!clazz.isInterface()) {
                    if (XEvent.class.isAssignableFrom(clazz)) {
                        eventIdList.add(EventId.of((Class<? extends XEvent>) clazz));
                    } else {
                        XEventArg annotation = clazz.getAnnotation(XEventArg.class);
                        if(annotation != null) {
                            Class<? extends XEvent>[] events = annotation.event();
                            for(Class<? extends XEvent> event: events) {
                                eventIdList.add(EventId.of(event, clazz));
                            }
                        }

                        for(Class<?> interfaceClass: clazz.getInterfaces()) {
                            XEventArg annotationOnInterface = interfaceClass.getAnnotation(XEventArg.class);
                            if(annotationOnInterface != null) {
                                Class<? extends XEvent>[] events = annotationOnInterface.event();
                                for(Class<? extends XEvent> event: events) {
                                    eventIdList.add(EventId.of(event, clazz));
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private SetMultimap<EventId, MethodInfo> methodMap = HashMultimap.create();

    private String name;

    public XEventBus() {
    }

    public XEventBus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<EventId> getEventIdList() {
        return eventIdList;
    }

    public void register(Object object) {

        if(targetSet.containsKey(object)) {
            throw new RuntimeException(String.format("事件监听对象%s重复注册", object));
        }

        Method[] methods = object.getClass().getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Subscribe.class)) {
                if (m.getParameterTypes().length == 1) {
                    methodMap.put(EventId.of((Class<? extends XEvent>)m.getParameterTypes()[0]), new MethodInfo(object, m));
                } else if(m.getParameterTypes().length == 2) {
                    methodMap.put(EventId.of((Class<? extends XEvent>)m.getParameterTypes()[0], m.getParameterTypes()[1]), new MethodInfo(object, m));
                } else {
                    throw new RuntimeException("不允许监听方法有超过两个参数");
                }
            }
        }
    }

    public void subscribe(Class<? extends XEvent> event, Object object, Method m) {
        if (m.getParameterTypes().length == 1) {
            Predicates.ensureTrue(m.getParameterTypes()[0].isAssignableFrom(event), String.format("%s不能转换为%s", event, m.getParameterTypes()[0]));
            methodMap.put(EventId.of(event), new MethodInfo(object, m));
        } else {
            throw new RuntimeException("不允许监听方法有超过一个参数");
        }
    }

    public void subscribe(Class<? extends XEvent> event, Class<?> eventArgs, Object object, Method m) {
        if(m.getParameterTypes().length == 2) {
            Predicates.ensureTrue(m.getParameterTypes()[0].isAssignableFrom(event), String.format("%s不能转换为%s", event, m.getParameterTypes()[0]));
            Predicates.ensureTrue(m.getParameterTypes()[1].isAssignableFrom(eventArgs), String.format("%s不能转换为%s", eventArgs, m.getParameterTypes()[1]));
            methodMap.put(EventId.of((Class<? extends XEvent>)m.getParameterTypes()[0], m.getParameterTypes()[1]), new MethodInfo(object, m));
        } else {
            throw new RuntimeException("不允许监听方法有超过两个参数");
        }
    }

    public void post(XEvent event) throws XEventBusHandlerException {
        try {
            if(trace) {
                if(this.eventStack.get() == null) {
                    this.eventStack.set(new Stack<>());
                }
                this.eventStack.get().push(event.getClass().getName());
                log.debug("触发事件[{}]", this.getCurrentEvent());
            }

            for(EventId eventId: methodMap.keySet()) {
                if(eventId.getEventClass().isAssignableFrom(event.getClass())) {
                    Collection<MethodInfo> collection = methodMap.get(eventId);

                    if (collection != null) {
                        for (MethodInfo methodInfo : collection) {
                            log.debug("调用方法：{}.{}", methodInfo.getMethod().getDeclaringClass().getName(), methodInfo.getMethod().getName());
                            methodInfo.invoke(event);

                        }
                    }
                }
            }

        } catch(RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new XEventBusHandlerException(e);
        } finally {
            if(trace) {
                log.debug("事件处理结束[{}]", this.getCurrentEvent());
                this.eventStack.get().pop();
            }
        }
    }

    private String getCurrentEvent() {
        return Optional.of(this.eventStack.get()).map(x-> String.join("->", x)).orElse("");
    }

    public void post(XEvent event, Object eventArg) throws XEventBusHandlerException {
        try {
            if(eventArg == null) {
                return;
            }
            if(trace) {
                if(this.eventStack.get() == null) {
                    this.eventStack.set(new Stack<>());
                }

                String eventName = String.format("%s(%s)", event.getClass(), eventArg.getClass());
                this.eventStack.get().push(eventName);
                log.debug("触发事件[{}]", this.getCurrentEvent());
            }

            for(EventId eventId: methodMap.keySet()) {
                if(eventId.getEventArgClass() != null) {
                    if(eventId.getEventClass().isAssignableFrom(event.getClass()) && eventId.getEventArgClass().isAssignableFrom(eventArg.getClass())) {
                        Collection<MethodInfo> collection = methodMap.get(eventId);

                        if (collection != null) {
                            for (MethodInfo methodInfo : collection) {
                                log.debug("调用方法：{}.{}", methodInfo.getMethod().getDeclaringClass().getName(), methodInfo.getMethod().getName());
                                methodInfo.invoke(event, eventArg);
                            }
                        }
                    }
                }
            }
        } catch(RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new XEventBusHandlerException(e);
        } finally {
            if(trace) {
                log.debug("事件处理结束[{}]", this.getCurrentEvent());
                this.eventStack.get().pop();
            }
        }
    }
}
