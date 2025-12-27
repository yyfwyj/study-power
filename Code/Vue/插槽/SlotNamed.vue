<template>
  <section class="named-slot">
    <div class="named-slot__header">
      <slot name="header" />
    </div>
    <div class="named-slot__body">
      <slot />
    </div>
    <div class="named-slot__footer">
      <slot name="footer" />
    </div>
  </section>
</template>

<style>
.named-slot {
  border-radius: 14px;
  border: 1px solid #e7e0d9;
  overflow: hidden;
}

.named-slot__header,
.named-slot__footer {
  background: #f3efe9;
  padding: 12px 16px;
}

.named-slot__body {
  padding: 16px;
  background: #ffffff;
}
</style>
