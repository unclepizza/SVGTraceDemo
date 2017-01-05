# SVGTraceDemo
展示如何使用SVG路径，动态绘制曲线，形成精致的追踪特效。

上效果图：

![这里写图片描述](http://img.blog.csdn.net/20170105162703940?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMjcyNTg3OTk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

效果图一共绘制了两条路径，一个SVG本身路径，一个是高亮的追逐效果。

动态绘制曲线是用 `DashPathEffect(float intervals[], float phase)`完成。

这个API，很多人不熟悉，举个例子：
```java
Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

p.setStyle(Style.STROKE);

p.setColor(Color.WHITE);

p.setStrokeWidth(1);

PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);

p.setPathEffect(effects);

canvas.drawLine(0, 40, mWidth, 40, p);

```

代码中的float数组，必须是偶数长度，且>=2，指定了多少长度的实线之后再画多少长度的空白。

如本代码中，绘制长度1的实线，再绘制长度2的空白，再绘制长度4的实线,再绘制长度8的空白，依次

重复。phase是起始位置的偏移量，代码中偏移量为1。

---

详见我的CSDN博客：[使用SVG路径图制作线追踪特效](http://blog.csdn.net/qq_27258799/article/details/54095831#svg%E8%B7%AF%E5%BE%84)




