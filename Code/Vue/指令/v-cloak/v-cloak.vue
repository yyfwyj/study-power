<template>
  <main class="page">
    <header class="hero">
      <div class="hero__tag">Vue 3</div>
      <h1>v-cloak 指令</h1>
      <p class="hero__subtitle">在模板尚未编译前隐藏插值，避免页面闪烁。</p>
    </header>

    <section class="card">
      <div class="card__header">
        <h2 class="card__title">用法概览</h2>
        <span class="pill pill--accent">编译前隐藏</span>
      </div>
      <p class="card__desc">
        v-cloak 会在 Vue 完成编译前保留在元素上，通过简单的 CSS 选择器隐藏它，待组件挂载后 Vue 自动移除该属性，元素随之显示。
      </p>
      <div class="note">
        <h3 class="note__title">核心要点</h3>
        <ul class="note__list">
          <li>CSS 写法：<code>[v-cloak] { display: none; }</code></li>
          <li>Vue 挂载后自动移除 v-cloak 属性，无需手动控制。</li>
          <li>更适用于直接用 CDN 引入 Vue 的页面；工程化项目通常不会闪烁。</li>
        </ul>
      </div>
    </section>

    <section class="card">
      <div class="card__header">
        <h2 class="card__title">示例</h2>
        <span class="pill">实时</span>
      </div>
      <div class="demo-grid">
        <div class="demo">
          <div class="demo__label">带 v-cloak 的元素</div>
          <div class="badge" v-cloak>{{ message }}</div>
          <p class="demo__hint">编译前被隐藏，挂载后显示消息。</p>
        </div>
        <div class="demo demo--muted">
          <div class="demo__label">代码写法</div>
          <pre class="code" v-pre>
&lt;div v-cloak&gt;{{ message }}&lt;/div&gt;
&lt;style&gt;
[v-cloak] { display: none; }
&lt;/style&gt;</pre>
        </div>
      </div>
    </section>

    <section class="card">
      <div class="card__header">
        <h2 class="card__title">使用场景</h2>
        <span class="pill pill--soft">最佳实践</span>
      </div>
      <div class="note note--inline">
        <ul class="note__list">
          <li>CDN 直接引入 Vue，需要等待脚本与模板编译时。</li>
          <li>在静态站点或示例页面中，避免模板插值裸露。</li>
          <li>工程化脚手架通常有构建、预渲染支持，可无需额外处理。</li>
        </ul>
      </div>
    </section>
  </main>
</template>

<script setup>
const message = 'Hello, Vue!';
</script>

<style>
:root {
  --bg: #f6f3ef;
  --card: #ffffff;
  --text: #2a2a2a;
  --muted: #6b6b6b;
  --accent: #d6785e;
  --accent-2: #2c6b7c;
  --border: #e4ded8;
}

[v-cloak] {
  display: none;
}

.page {
  min-height: 100vh;
  background: radial-gradient(circle at top, #fef6ea 0%, #f6f3ef 45%, #eef0f2 100%);
  color: var(--text);
  font-family: "Noto Sans SC", "Source Han Sans SC", "PingFang SC", "Microsoft YaHei", sans-serif;
  padding: 32px 20px 64px;
  display: grid;
  gap: 20px;
}

.hero {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 28px 24px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
}

.hero h1 {
  font-size: 28px;
  margin: 12px 0 8px;
}

.hero__subtitle {
  color: var(--muted);
  line-height: 1.6;
}

.hero__tag {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 999px;
  background: rgba(214, 120, 94, 0.15);
  color: var(--accent);
  font-weight: 600;
  font-size: 12px;
  letter-spacing: 0.5px;
}

.card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 22px;
  display: grid;
  gap: 12px;
}

.card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.card__title {
  font-size: 20px;
}

.card__desc {
  color: var(--muted);
  line-height: 1.6;
}

.note {
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 14px 16px;
  background: #ffffff;
  display: grid;
  gap: 8px;
}

.note--inline {
  padding: 12px 14px;
}

.note__title {
  font-size: 12px;
  color: var(--accent-2);
  letter-spacing: 0.6px;
  font-weight: 600;
}

.note__list {
  padding-left: 18px;
  color: var(--muted);
  line-height: 1.7;
}

.demo-grid {
  display: grid;
  gap: 14px;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
}

.demo {
  border: 1px dashed var(--border);
  border-radius: 14px;
  padding: 16px;
  display: grid;
  gap: 10px;
  background: #faf9f8;
}

.demo--muted {
  background: #f1f1f1;
}

.demo__label {
  font-weight: 600;
  color: var(--accent-2);
}

.demo__hint {
  color: var(--muted);
  line-height: 1.5;
}

.pill {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  border-radius: 999px;
  background: rgba(44, 107, 124, 0.12);
  color: var(--accent-2);
  font-weight: 600;
  font-size: 12px;
}

.pill--accent {
  background: rgba(214, 120, 94, 0.14);
  color: var(--accent);
}

.pill--soft {
  background: rgba(44, 107, 124, 0.08);
}

.badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 10px 14px;
  border-radius: 12px;
  background: rgba(214, 120, 94, 0.14);
  color: var(--accent);
  font-weight: 700;
  min-height: 44px;
  box-shadow: inset 0 0 0 1px rgba(214, 120, 94, 0.24);
}

.code {
  background: #0d1117;
  color: #d1d5db;
  padding: 12px;
  border-radius: 10px;
  font-size: 13px;
  line-height: 1.6;
  overflow: auto;
  font-family: "JetBrains Mono", "Fira Code", Consolas, monospace;
  border: 1px solid #1f2a37;
}
</style>
