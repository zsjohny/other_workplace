1. TestNG与maven集成时，最好按照以下规则命名。
测试类：*Test.java
测试方法：test*
在集成时，如果maven surefire plugin没指定suiteXmlFiles，那么maven会寻找符合上述命名规则的测试类和方法。
如果指定了suiteXmlFiles，上面的命名不是必须的，但最好使用这种命名。

2. skip tests
You can also skip the tests via command line by executing the following command:
mvn install -DskipTests （这个似乎没用。因此在maven surefire plugin中默认这个值设为false，或者不声明）
If you absolutely must, you can also use the maven.test.skip property to skip compiling the tests. maven.test.skip is honored by Surefire, Failsafe and the Compiler Plugin.
mvn install -Dmaven.test.skip=true

3. web层单元测试，可以使用spring-mock。