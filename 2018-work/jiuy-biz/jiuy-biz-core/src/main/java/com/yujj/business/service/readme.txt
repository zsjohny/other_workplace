service层用于一些基础服务

1. 不可依赖其他层级。
2. 避免依赖其他业务service（但业务service可以依赖common service）。如果一个方法必须依赖其他业务service，这个方法应该往上移到facade、web层。
3. 避免对象的装配。如果一个方法需要装配对象，这个方法应该往上移到assembler、facade、web层。