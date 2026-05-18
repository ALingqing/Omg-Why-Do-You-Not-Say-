# OMG, WHY DO YOU NOT SAY!!!!!!!

#我去！！！不早说！！！

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-blue)
![Paper](https://img.shields.io/badge/Paper-1.20.1-brightgreen)
![Folia](https://img.shields.io/badge/Folia-%E2%9C%93-supported)

[![pexKX59.jpg](https://s41.ax1x.com/2026/05/18/pexKX59.jpg)](https://imgchr.com/i/pexKX59)

> 一个"延迟"检测玩家动作并发送夸张提示的 Minecraft Paper 插件。
此插件模仿百科模组 [mcmod.cn](https://www.mcmod.cn/class/26019.html)
当玩家在服务器中破坏方块、放置方块或被攻击时，插件会在延迟数秒后向该玩家发送一条醒目的夸张提示消息 —— "你在 X 秒前挖了一个 XXX ！！！！"，营造出一种"后知后觉"的幽默效果。

[![pexKvCR.png](https://s41.ax1x.com/2026/05/18/pexKvCR.png)](https://imgchr.com/i/pexKvCR)

[![pexKx81.png](https://s41.ax1x.com/2026/05/18/pexKx81.png)](https://imgchr.com/i/pexKx81)

##  功能特性

- **方块破坏检测** — 玩家破坏方块后延迟提示
- **方块放置检测** — 玩家放置方块后延迟提示
- **攻击检测** — 玩家被攻击后延迟提示
- **多语言支持** — 内置 50+ 种语言文件，自动根据客户端语言显示对应消息
- **可配置延迟** — 延迟秒数可在 1~10 秒之间自由调整
- **醒目消息** — 红色加粗文字，夸张语气，效果拉满
- **Folia 兼容** — 完美支持 Folia 区域化线程调度
- **高度可配** — 每个功能均可独立开关

##  安装

1. 从 [Releases](https://github.com/ALingqing/omg_why_do_you_not_say/releases) 下载最新版本的 `OMG_WHY_DO_YOU_NOT_SAY-{version}.jar`
2. 将 jar 文件放入服务器的 `plugins/` 目录
3. 重启服务器或执行 `/reload`
4. 插件将自动生成默认配置文件

##  配置

配置文件位于 `plugins/OMG_WHY_DO_YOU_NOT_SAY/config.yml`：

```yaml
# 是否启用插件
enabled: true

# 延迟秒数 (1-10)
delaySeconds: 3

# 通知方块破坏
notifyBreak: true

# 通知方块放置
notifyPlace: true

# 通知受到攻击
notifyAttack: true

# 语言设置 (auto = 根据客户端语言自动选择)
# 可选: zh_cn, zh_tw, en_us 等
language: auto
```

> **提示**：修改配置后无需重启服务器，执行 `/reload` 或 `/omgreload`（如果有）即可生效。

## 🌍 语言支持

插件支持自动检测玩家客户端语言并显示对应消息。支持的语言包括但不限于：

| 语言 | 文件名 |
|------|--------|
| 🇨🇳 简体中文 | `zh_cn.yml` |
| 🇹🇼 繁体中文 | `zh_tw.yml` |
| 🇭🇰 香港繁体 | `zh_hk.yml` |
| 🇺🇸 英语 (美国) | `en_us.yml` |
| 🇬🇧 英语 (英国) | `en_gb.yml` |
| 🇯🇵 日语 | `ja_jp.yml` |
| 🇰🇷 韩语 | `ko_cn.yml` |
| 🇫🇷 法语 | `fr_fr.yml` |
| 🇩🇪 德语 | `de_de.yml` |
| 🇪🇸 西班牙语 | `es_es.yml` |
| 🇷🇺 俄语 | `ru_ru.yml` |
| 🏴󠁥󠁳󠁣󠁴󠁿 海盗语 | `en_pirate.yml` |
| ... 以及更多！ | |

## 🛠️ 构建

### 环境要求

- **JDK 17** 或更高版本
- **Maven** 3.6+

### 构建步骤

```bash
# 克隆仓库
git clone https://github.com/ALingqing/omg_why_do_you_not_say.git

# 进入目录
cd omg_why_do_you_not_say

# 构建插件
mvn clean package
```

构建产物将位于 `target/OMG_WHY_DO_YOU_NOT_SAY-{version}.jar`。

## 📋 依赖

- **Paper API 1.20.1-R0.1-SNAPSHOT**（provided）
- 兼容 Spigot / Paper / Folia / Purpur 等服务端

## 📄 开源许可

本项目采用 [MIT License](LICENSE) 开源。

## 👤 作者

- **阿清ALingqing_** — [GitHub](https://github.com/ALingqing)

---

> 如果这个插件给你带来了欢乐，不妨 ⭐ 一个吧！
