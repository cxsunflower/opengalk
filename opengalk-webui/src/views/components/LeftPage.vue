<template>
  <div class="left">
    <div class="left-top">
      <div>
        <img alt="" class="left-logo" src="../../assets/logo/vite.svg"/>
      </div>
      <div><span class="left-title">OpenGALK</span></div>
    </div>

    <div class="left-background">
      <img alt="" class="background" src="../../assets/images/login-box-bg.svg">
    </div>

    <div class="footer">
      <span class="footer-font">xxxxxxxxxxxxx</span>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.left {
  float: left;
  width: 50vw;
  height: 100vh;
  background-color: #353e54;

  .left-top {
    margin: 40px 40px 40px 60px;
    display: flex;
    flex-direction: row;

    .left-logo {
      height: 60px;
      width: 60px;
    }

    .left-title {
      margin-left: 40px;
      color: white;
      font-size: 60px;
    }
  }

  .left-background {
    text-align: center;
    width: 50vw;

    .background {
      width: 50vw;
      height: 50vh;
    }
  }

  .footer {
    margin-top: 100px;
    text-align: center;

    .footer-font {
      color: white;
      font-size: 30px;
    }
  }
}
</style>
