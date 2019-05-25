<template>
  <div v-show="show" @click="download" class="advertising">
    <div class="left"></div>
    <div class="center">无视黑白户，贷款就找它</div>
    <div @click="close" class="right"></div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        show: true
      }
    },
    watch: {
      $route: function() {
        this.show = true
      }
    },
    methods: {
      close: function(e) {
        this.show = false
        e = e || event
        e.stopPropagation();
      },
      download: function() {
        window.location.href = 'https://a.app.qq.com/o/simple.jsp?pkgname=com.xdd.wubai&channel=0002160650432d595942&fromcase=60001'
      }
    }
  }
</script>
