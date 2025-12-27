<template>
  <section class="scoped-slot">
    <slot name="data" :demoText="demoText"></slot>
  </section>
</template>

<script setup>
const demoText = 'Hello from scoped slot!';
</script>

<style>
.scoped-slot {
  border-radius: 12px;
  border: 1px solid #e7e0d9;
  background: #ffffff;
  padding: 14px;
}
</style>
