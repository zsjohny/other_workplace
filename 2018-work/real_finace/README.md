# finace
#目前为一桶金的项目迭代
##API 接口文档规范
1. 需写上Controller注释-写在`@RestController`上方
2. Controller类的方法上方写`/** */`注释
3. Controller类的方法 内部 不得使用`/** */`注释
4. Controller类的方法上方  第一个注解应为`@RequestMapping` 或 `@PostMapping` 或 `@GetMapping`
5. Controller类中只能写简单的逻辑判断 不得将 业务写在Controller中
6. Controller类中不得抛出明细的异常  如：`throw  Exception`
7. Controller类的 `/** */`注释中 应写类注释名称 参数名称 返回值 参数名称下方应空一行 再写 返回值 且注释需格式化
* 若有 参数名称 则

    例如： 
``` 
    /**
            * 消息详情列表
            *
            * @param page    页码
            * @param msgCode 消息类型
            *                <p>
            *                {
            *                "msg": "",
            *                "code": 200,
            *                "data": {
            *                "0": {
            *                "msgCode": 0,
            *                "topic": "0",
            *                "msg": "0",
            *                "addtime": 1521531989433
            *                },
            *                "2": {
            *                "msgCode": 2,
            *                "topic": "2",
            *                "msg": "2",
            *                "addtime": 1521531981433
            *                }
            *                }
            *                }
            * @return
            */
```    
 
 * 若无 参数名称 则
 
    例如：
```   
         /**
          * 消息详情列表
          *                <p>
          *                {
          *                "msg": "",
          *                "code": 200,
          *                "data": {
          *                "0": {
          *                "msgCode": 0,
          *                "topic": "0",
          *                "msg": "0",
          *                "addtime": 1521531989433
          *                },
          *                "2": {
          *                "msgCode": 2,
          *                "topic": "2",
          *                "msg": "2",
          *                "addtime": 1521531981433
          *                }
          *                }
          *                }
          * @return
          */      
```    
## 代码规范 
1. Service层不允许抛出异常 `throw Exception`
2. Service层使用 `try cash`捕获异常
3. Dao层不处理异常 直接抛出异常 `throw Exception`
4. logger日志的使用中 只有在`cash`的时候 才能使用 最高级别的`error` 其余对于错误的处理应 使用`warn`