# zayboot-framework
### 一个仿SpringBoot的Java框架

#### 意图

编写这个东西纯粹是兴趣所在。学习了Springboot，但是总觉得知其然而不知其所以然，于是就想自己造一个这样的轮子，让自己理解得更加深刻。

功能上我想着只要把我自己常用的一些注解以及Spring的核心Ioc和Aop实现就行了，其他的再慢慢加嘛，毕竟时间和精力有限。


#### 预计实现的功能(目标)

- 控制反转Ioc
  - @Component
  - @Autowired
  - @Qualifier
- 切面Aop
  - @After
  - @Aspect
  - @Before
  - @Pointcut
- 控制层Mvc
  - @GetMapping
  - @PostMapping
  - @RequestBody
  - @RequestParam
  - @RestController
  - @PathVariable
- 启动类boot
  - @SpringbootApplication
  - @ComponentScan