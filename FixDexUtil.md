# 热修复核心

```
/**
 * 合并dex
 *
 * @param appContext
 * @param loadedDex
 */
private static void doDexInject(Context appContext, HashSet<File> loadedDex) {
    String optimizeDir = appContext.getFilesDir().getAbsolutePath() +
            File.separator + OPTIMIZE_DEX_DIR;
    // data/data/包名/files/optimize_dex（这个必须是自己程序下的目录）

    File fopt = new File(optimizeDir);
    if (!fopt.exists()) {
        fopt.mkdirs();
    }
    try {
        // 1.加载应用程序dex的Loader
        PathClassLoader pathLoader = (PathClassLoader) appContext.getClassLoader();
        for (File dex : loadedDex) {
            // 2.加载指定的修复的dex文件的Loader
            DexClassLoader dexLoader = new DexClassLoader(
                    dex.getAbsolutePath(),// 修复好的dex（补丁）所在目录
                    fopt.getAbsolutePath(),// 存放dex的解压目录（用于jar、zip、apk格式的补丁）
                    null,// 加载dex时需要的库
                    pathLoader// 父类加载器
            );
            // 3.开始合并
            // 合并的目标是Element[],重新赋值它的值即可

            /**
             * BaseDexClassLoader中有 变量: DexPathList pathList
             * DexPathList中有 变量 Element[] dexElements
             * 依次反射即可
             */

            //3.1 准备好pathList的引用
            Object dexPathList = getPathList(dexLoader);
            Object pathPathList = getPathList(pathLoader);
            //3.2 从pathList中反射出element集合
            Object leftDexElements = getDexElements(dexPathList);
            Object rightDexElements = getDexElements(pathPathList);
            //3.3 合并两个dex数组
            Object dexElements = combineArray(leftDexElements, rightDexElements);

            // 重写给PathList里面的Element[] dexElements;赋值
            Object pathList = getPathList(pathLoader);// 一定要重新获取，不要用pathPathList，会报错
            setField(pathList, pathList.getClass(), "dexElements", dexElements);
        }
        Toast.makeText(appContext, "修复完成", Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```