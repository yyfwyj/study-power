<template>
  <section class="slot-child">
    <header class="slot-child__title">子组件</header>
    <div class="slot-child__body">
      <slot>
        <p class="slot-child__placeholder">默认内容：父组件没有提供插槽内容时显示。</p>
      </slot>
    </div>
    <footer class="slot-child__note">子组件内部数据：{{ message2 }}</footer>
  </section>
</template>

<script setup>
const message2 = 'Hello, from child!';
</script>

<style>
.slot-child {
  border-radius: 12px;
  border: 1px solid #e7e0d9;
  background: #fffefb;
  padding: 14px;
  display: grid;
  gap: 8px;
}

.slot-child__title {
  font-weight: 600;
  color: #2c6b7c;
}

.slot-child__body {
  color: #333;
}

.slot-child__placeholder {
  color: #8a7f75;
}

.slot-child__note {
  font-size: 12px;
  color: #9a8f85;
}
</style>
