<template>
  <main class="page">
    <header class="hero">
      <div class="hero__tag">Vue 3</div>
      <h1>v-pre 指令</h1>
      <p class="hero__subtitle">跳过编译，直接输出原始内容，适合展示模板代码。</p>
    </header>

    <section class="card">
      <div class="card__header">
        <h2 class="card__title">用法概览</h2>
        <span class="pill pill--accent">跳过编译</span>
      </div>
      <p class="card__desc">
        v-pre 会阻止当前元素及子节点被 Vue 编译，插值、指令都会被原样输出。常用于文档、示例场景，或在少量节点上避开不必要的解析。
      </p>
      <div class="note">
        <h3 class="note__title">核心要点</h3>
        <ul class="note__list">
          <li>元素及子树的插值与指令不再解析，直接渲染文本。</li>
          <li>适合展示代码片段，或避免模板表达式被 Vue 处理。</li>
          <li>不要包裹需要交互/数据驱动的内容，否则逻辑被完全跳过。</li>
        </ul>
      </div>
    </section>

    <section class="card">
      <div class="card__header">
        <h2 class="card__title">示例</h2>
        <span class="pill">对比</span>
      </div>
      <div class="demo-grid">
        <div class="demo">
          <div class="demo__label">正常渲染</div>
          <div class="badge">{{ message }}</div>
          <p class="demo__hint">双花括号会被编译成数据值。</p>
        </div>
        <div class="demo demo--muted">
          <div class="demo__label">使用 v-pre</div>
          <div class="badge" v-pre>{{ message }}</div>
          <p class="demo__hint">插值被当作普通文本输出，不执行编译。</p>
        </div>
        <div class="demo">
          <div class="demo__label">代码写法</div>
          <pre class="code" v-pre>
&lt;div v-pre&gt;{{ message }}&lt;/div&gt;
&lt;!-- 插值与指令都会被跳过 --&gt;</pre>
        </div>
      </div>
    </section>

    <section class="card">
      <div class="card__header">
        <h2 class="card__title">注意事项</h2>
        <span class="pill pill--soft">避免误用</span>
      </div>
      <div class="note note--inline">
        <ul class="note__list">
          <li>不要在根节点上使用 v-pre，否则整棵应用无法编译。</li>
          <li>仅在小范围、明确不需要响应式的内容上使用。</li>
          <li>展示代码时可搭配 <code>v-pre</code> 与样式让文本更易读。</li>
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
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
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
  background: rgba(44, 107, 124, 0.14);
  color: var(--accent-2);
  font-weight: 700;
  min-height: 44px;
  box-shadow: inset 0 0 0 1px rgba(44, 107, 124, 0.24);
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
