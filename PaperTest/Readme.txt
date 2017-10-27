1.src：源代码
   1）algorithm 算法的相关代码
   2）script 调用算法及数据预处理的相关代码

2.script：脚本
   java的jar包及shell脚本，可直接运行，已打包
   参数格式：dataset目录(其中定义训练集词向量为train_x.txt,训练集标签为train_y.txt,测试集词向量为test_x.txt,测试集标签为test_y.txt) 训练样本个数 测试样本个数 初始向量维度 标签类别数量 层数(之前代码为方便定义为3层，后期已update，但jar包还未update) 各隐藏层神经元个数 稀疏性参数 稀疏惩罚(后2个参数根据不同种类的模型而定)

3.data：数据文件
  data/SA/text/ : 本实验所用数据
  data/SA/text/originalData/ : raw data
  data/SA/text/1 ; raw data 预处理后的数据

4.log：实验结果

5.lib：程序需要的外部依赖包