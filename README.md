#教你如何写框架------用中文构建脚本

用中文写脚本，完全弃掉IDE，就能完成java+selenium的自动化测试过程，体验一下这神奇的过程吧。

我们先来看下面的scenario:

1.打开百度

2.输入搜索关键字并点击"百度一下"

3.在搜索结果页面取出搜索输入框的值进行验证

我们来看如何实现：

1.在TestBaidu.yaml中保存好locator（保存方法在前面有介绍）

2.在TestBaidu.xml保存好测试数据

3.在TestBaidu.txt中写上如下的脚本：

　　1."打开"{url}

　　2."页面"{testBaidu},输入框(baidu_input)[sendKeys]{hello world}

　　3."页面"{testBaidu},[click]按钮(baidu_button)

　　4."页面"{testBaidu},取得输入框(baidu_input1)属性值[getAttribute]{attrValue},"返回值"{getInput}

　　5."对象"{Log},输出[信息]{getInput}

　　6."对象"{Assert},判断[assertEquals]{getInput,assertValue}

整个过程就完了，就可以直接运行了，在这个过程中完全没有用到eclipse或其它的IDE，如果细心的人，应该很快能发现上面写脚本的方式很适合于在excel或平台中使用，留给大家去扩展吧。

下面介绍一下几个特点：

1.对于Locator的保存方式，支持参数化，比如

baidu_input:<br>
  type: id<br>
  value: %s

也就是说这个%s要是脚本中被替换掉，于是我们可以这样使用2."页面"{testBaidu},输入框(baidu_input){kw}[sendKeys]{hello world},如果有多个参数要替换，用逗号隔开。

2.对于测试数据，在脚本中一般用{}符号概括，在{hello}中，如果hello在xml文件中有结点存在，则取结点的值作为数据，如果在xml文件中没有结点存在，则数据就为hello.

3.对于有一些公共方法，要被抽象出去，抽象出来的，也放在txt中，也用中文来写，即一个txt文件就代表一个方法，但是java是面向对象的，这些个方法要有对象，所以我把这些方法与page联系在了一起，联系方式为：

LinkedPages.xml:

<Pages>
    <TestBaidu>
        <methohd name='search' return='' linked='TestBaidu1'/>
    </TestBaidu>    
</Pages>

TestBaidu就是代表页面page,里面有一个search方法，具体的实现在TestBaidu1.txt中，没有返回值（return的值为空或者没有return属性）

4.当然我们也可以自已去扩展page，用代码来写page里面的方法，这些page放在com.test.page包下面，如果page里有search方法，LinkedPages.xml里面也有search方法，则以page里的优先。

5.支持逻辑判断与循环，例如：

　　1."如果"{1}等于{1},"并且"{2}不等于{3}
　　-"对象"{Log},输出[信息]{a}
　　-"如果"{1}等于{1}
　　--"循环"{i}从{1}到{3}
　　---"对象"{Log},输出[信息]{i}
　　2."循环"{i}从{1}到{3}
　　-"对象"{Log},输出[信息]{i}

提供了4个接口，接口说明：

　　1.Locators接口，如果你不想用yaml来保存Locators，想用DB或者其它文件，只需实现这个接口就行了

　　2.TestDatas接口，如果你不想用xml来保存数据，只需实现该接口就可以了

　　3.Linked接口，如果不想用LinkedPages.xml，也要实现该接口

　　4.TestCases接口，如果觉得TXT不直观，实现一下该接口就OK了

备注：以上4个接口实现后都得在config.xml里配置一下。

如果你想实现平台，或者在领导面前炫一把，用该框架，应该会屌炸天。

 下载地址：http://files.cnblogs.com/zhangfei/Automation2.0.rar

交流平台：QQ群254285583,  174527142
