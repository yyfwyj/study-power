<template>
  <section class="conditional-slot">
    <div v-if="$slots.header" class="conditional-slot__header">
      <slot name="header" />
    </div>

    <div v-if="$slots.default" class="conditional-slot__main">
      <slot />
    </div>

    <div v-if="$slots.footer" class="conditional-slot__footer">
      <slot name="footer" />
    </div>
  </section>
</template>

<style>
.conditional-slot {
  border-radius: 14px;
  border: 1px dashed #e0d8cf;
  background: #fbfaf8;
  display: grid;
  gap: 10px;
  padding: 16px;
}

.conditional-slot__header {
  background: #f5d9cf;
  padding: 10px 12px;
  border-radius: 10px;
}

.conditional-slot__main {
  background: #fff6da;
  padding: 10px 12px;
  border-radius: 10px;
}

.conditional-slot__footer {
  background: #d7efe3;
  padding: 10px 12px;
  border-radius: 10px;
}
</style>
