<template>
  <main class="slot-page">
    <header class="hero">
      <div class="hero__tag">Vue 3</div>
      <h1>插槽 Slot 学习页</h1>
      <p class="hero__subtitle">
        用更清晰的结构演示默认插槽、具名插槽、条件插槽、动态插槽名和作用域插槽。
      </p>
    </header>

    <section class="card">
      <h2 class="card__title">渲染作用域</h2>
      <p class="card__desc">
        插槽内容是在父组件中定义的，所以能访问父组件数据，但访问不到子组件内部数据。
      </p>
      <div class="note">
        <h3 class="note__title">要点速记</h3>
        <ul class="note__list">
          <li>插槽内容编译在父组件作用域中，天然继承父组件数据。</li>
          <li>子组件只负责渲染插槽位置，不会把自身私有状态暴露给插槽内容。</li>
          <li>需要从子组件向外传值时，用作用域插槽（slot props）。</li>
        </ul>
      </div>
      <div class="demo">
        <div class="demo__head">父组件数据：{{ message }}</div>
        <SlotDefaultChild>
          <p class="demo__text">slot 内容里可以直接用父组件数据：{{ message }}</p>
        </SlotDefaultChild>
      </div>
      <div class="demo demo--muted">
        <SlotDefaultChild>
          <p class="demo__text">子组件数据 message2 在这里是不可用的。</p>
        </SlotDefaultChild>
      </div>
    </section>

    <section class="card">
      <h2 class="card__title">默认内容</h2>
      <p class="card__desc">不传内容时显示子组件默认插槽内容，传入时则被替换。</p>
      <div class="note">
        <h3 class="note__title">要点速记</h3>
        <ul class="note__list">
          <li>子组件 <code>&lt;slot&gt;</code> 内的内容是后备内容（fallback）。</li>
          <li>父组件传入插槽内容后，会替换后备内容。</li>
          <li>适合占位说明、空状态提示、默认标题等可选内容。</li>
        </ul>
      </div>
      <div class="grid">
        <div class="demo">
          <h3 class="demo__title">未传入内容</h3>
          <SlotDefaultChild />
        </div>
        <div class="demo">
          <h3 class="demo__title">传入内容</h3>
          <SlotDefaultChild>
            <p class="demo__text">这是父组件传入的默认插槽内容。</p>
          </SlotDefaultChild>
        </div>
      </div>
    </section>

    <section class="card">
      <h2 class="card__title">具名插槽</h2>
      <p class="card__desc">通过 name 指定插槽位置，配合 # 语法更直观。</p>
      <div class="note">
        <h3 class="note__title">要点速记</h3>
        <ul class="note__list">
          <li>每个插槽用 <code>name</code> 标识位置，父组件用 <code>#name</code> 传入。</li>
          <li>没有 name 的插槽等同于 <code>default</code>。</li>
          <li>父组件的插槽内容顺序不重要，最终按子组件插槽位置渲染。</li>
        </ul>
      </div>
      <SlotNamed class="demo">
        <template #header>
          <div class="slot-badge">Header</div>
        </template>
        <p class="demo__text">这里是默认插槽内容。</p>
        <template #footer>
          <div class="slot-badge slot-badge--accent">Footer</div>
        </template>
      </SlotNamed>
    </section>

    <section class="card">
      <h2 class="card__title">条件插槽</h2>
      <p class="card__desc">通过 $slots 判断是否渲染对应区域。</p>
      <div class="note">
        <h3 class="note__title">要点速记</h3>
        <ul class="note__list">
          <li>使用 <code>$slots.name</code> 判断父组件是否传入了该插槽。</li>
          <li>条件渲染适合隐藏空区域，避免出现多余的占位块。</li>
          <li>复杂布局可配合 <code>v-if</code> 或 <code>v-show</code> 细化显示逻辑。</li>
        </ul>
      </div>
      <SlotConditional class="demo">
        <template #header>
          <p class="demo__text">我只在 header 插槽存在时显示。</p>
        </template>
        <template #default>
          <p class="demo__text">默认插槽内容。</p>
        </template>
      </SlotConditional>
    </section>

    <section class="card">
      <h2 class="card__title">动态插槽名</h2>
      <p class="card__desc">根据状态决定内容进入哪个具名插槽。</p>
      <div class="note">
        <h3 class="note__title">要点速记</h3>
        <ul class="note__list">
          <li>动态插槽名使用 <code>#[变量]</code> 语法，变量值需要是已有插槽名。</li>
          <li>适合在不同状态下复用同一段内容到不同位置。</li>
          <li>通常配合 <code>ref</code> 或 <code>computed</code> 控制插槽入口。</li>
        </ul>
      </div>
      <div class="demo">
        <button class="btn" type="button" @click="toggleSlot">
          切换到 {{ currentSlot === 'header' ? 'footer' : 'header' }}
        </button>
        <SlotDynamic>
          <template #[currentSlot]>
            <p class="demo__text">当前插入到 {{ currentSlot }} 插槽。</p>
          </template>
        </SlotDynamic>
      </div>
    </section>

    <section class="card">
      <h2 class="card__title">作用域插槽</h2>
      <p class="card__desc">子组件向外暴露数据，父组件通过参数接收。</p>
      <div class="note">
        <h3 class="note__title">要点速记</h3>
        <ul class="note__list">
          <li>子组件用 <code>&lt;slot name="data" :demoText="..."&gt;</code> 暴露数据。</li>
          <li>父组件用 <code>#data="{ demoText }"</code> 接收并渲染。</li>
          <li>适合列表渲染、表格单元格等需要由父组件控制表现的场景。</li>
        </ul>
      </div>
      <SlotScoped class="demo">
        <template #data="{ demoText }">
          <p class="demo__text">子组件提供的数据：{{ demoText }}</p>
        </template>
      </SlotScoped>
    </section>
  </main>
</template>

<script setup>
import { ref } from 'vue';
import SlotDefaultChild from './SlotDefaultChild.vue';
import SlotNamed from './SlotNamed.vue';
import SlotConditional from './SlotConditional.vue';
import SlotDynamic from './SlotDynamic.vue';
import SlotScoped from './SlotScoped.vue';

const message = 'Hello, Vue!';
const currentSlot = ref('header');

const toggleSlot = () => {
  currentSlot.value = currentSlot.value === 'header' ? 'footer' : 'header';
};
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

.slot-page {
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
  padding: 24px;
  display: grid;
  gap: 14px;
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

.grid {
  display: grid;
  gap: 16px;
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

.demo__head {
  font-weight: 600;
  color: var(--accent-2);
}

.demo__title {
  font-size: 14px;
  color: var(--muted);
}

.demo__text {
  line-height: 1.6;
}

.slot-badge {
  display: inline-flex;
  padding: 6px 12px;
  border-radius: 999px;
  background: rgba(44, 107, 124, 0.12);
  color: var(--accent-2);
  font-weight: 600;
  font-size: 12px;
}

.slot-badge--accent {
  background: rgba(214, 120, 94, 0.14);
  color: var(--accent);
}

.btn {
  justify-self: start;
  border: 1px solid var(--border);
  background: #ffffff;
  color: var(--text);
  padding: 8px 14px;
  border-radius: 999px;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 18px rgba(0, 0, 0, 0.1);
}
</style>
