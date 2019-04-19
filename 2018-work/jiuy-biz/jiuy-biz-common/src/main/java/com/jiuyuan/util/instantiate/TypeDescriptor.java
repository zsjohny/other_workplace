package com.jiuyuan.util.instantiate;

public class TypeDescriptor {

    private Class<?> type;

    /**
     * The factory used to instanciate this type.
     */
    private Class<? extends InstanceFactory> factory;

    /**
     * A legal value that can be used to infect the instantiation process.
     */
    private String hint;

    public TypeDescriptor(Class<?> type, Class<? extends InstanceFactory> factory, String hint) {
        this.type = type;
        this.factory = factory;
        this.hint = hint;
    }

    public TypeDescriptor(Class<?> type) {
        this(type, DefaultInstanceFactory.class, null);
    }

    public Class<?> getType() {
        return type;
    }

    public Class<? extends InstanceFactory> getFactory() {
        return factory;
    }

    public String getHint() {
        return hint;
    }

    public boolean isVoidType() {
        return this.type == void.class || this.type == Void.class;
    }

    public Object createDefaultInstance() {
        InstanceFactory instanceFactory = CachingInstanciator.INSTANCE.instantiate(this.factory);
        return instanceFactory.instantiate(this.type, this.hint);
    }
}
