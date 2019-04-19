package com.jiuy.core.business.assemble;

public interface Assembler <T extends Composable<Long>>{

    public T assemble(T toBeAssemble);
}
