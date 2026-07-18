# Ruby Mod —— Minecraft 1.20.1 Forge 模组

一个用于学习的完整 Forge 模组示例，包含三类内容：

| 类型 | 内容 |
| --- | --- |
| 物品 | 红宝石 `ruby`、红宝石剑 `ruby_sword`、刷怪蛋 `ruby_golem_spawn_egg` |
| 方块 | 红宝石块 `ruby_block`、红宝石矿石 `ruby_ore`、深层红宝石矿石 `deepslate_ruby_ore` |
| 生物 | 红宝石傀儡 `ruby_golem`（被动生物，可用红宝石喂养繁殖，会在主世界自然生成） |

环境：**Minecraft 1.20.1 + Forge 47.2.0 + Java 17**。

---

## 一、环境准备

1. 安装 **JDK 17**（必须是 17，1.20.1 不支持更高/更低的大版本）。
   - 验证：`java -version` 应显示 17。
2. 安装 **IntelliJ IDEA**（社区版即可）。

## 二、导入并首次构建

> 因为本压缩包没有附带 `gradlew` 的二进制 wrapper（gradle-wrapper.jar），首次需要联网让 Gradle 自动补全依赖。

**方式 A（推荐，用 IDEA）**
1. IDEA → `File` → `Open`，选中本项目根目录（含 `build.gradle` 的那层）。
2. IDEA 会自动识别 Gradle 工程并开始下载 Forge / Minecraft 依赖（第一次较慢，需联网）。
3. 等右下角进度条跑完即可。

**方式 B（命令行）**
1. 本机若已装 Gradle：在项目根目录执行 `gradle wrapper` 生成 wrapper，再执行 `./gradlew build`。
2. 或直接 `gradle build`。

## 三、在游戏里测试

- 在 IDEA 右上角的运行配置里选择 **runClient**，点运行，会启动一个带本模组的 Minecraft 客户端。
- 进游戏后：
  - 创造模式物品栏会多出一个「红宝石模组 / Ruby Mod」标签页。
  - `/give @s rubymod:ruby_golem_spawn_egg` 可以直接拿到刷怪蛋生成红宝石傀儡。
  - 红宝石矿石会按原版铁矿石的逻辑（需要铁镐及以上）挖掘并掉落红宝石。

## 四、打包发布

```bash
./gradlew build
```
生成的 jar 在 `build/libs/rubymod-1.20.1-1.0.0.jar`，丢进 `mods` 文件夹即可被 Forge 加载。

---

## 五、改成你自己的模组

1. 改 `gradle.properties` 里的 `mod_id`、`mod_group_id`、`mod_name`、`mod_authors`。
2. 把 Java 源码包名 `net/tutorial/rubymod` 重命名为你的 `mod_group_id` 对应路径。
3. 把所有资源文件里的 `rubymod`（assets/data 文件夹名、json 内的 `rubymod:` 前缀）替换成你的新 modid。
4. `RubyMod.java` 里的 `MOD_ID` 常量同步修改。

## 六、目录速览

```
src/main/java/net/tutorial/rubymod/
├─ RubyMod.java                 主类，注册各注册器
├─ item/ModItems.java           物品
├─ item/ModCreativeModeTabs.java 创造模式标签页
├─ block/ModBlocks.java         方块（含自动注册 BlockItem）
├─ entity/ModEntities.java      实体类型注册
├─ entity/custom/RubyGolemEntity.java  生物逻辑/AI/属性
├─ entity/client/              生物模型与渲染器
└─ event/                      属性、刷怪规则、客户端渲染注册

src/main/resources/
├─ META-INF/mods.toml          模组元信息
├─ assets/rubymod/             贴图、模型、语言文件
└─ data/rubymod/               战利品表、生物群系刷怪修改器、标签
```

许可证：MIT。
