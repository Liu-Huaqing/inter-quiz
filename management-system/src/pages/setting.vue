<template>
  <div class="question-setting">
    <el-row class="tac">
      <el-col :span="3">
        <h3><router-link to="/">小木科技</router-link></h3>
        <el-menu
          default-active="2"
          class="el-menu-vertical">
          <el-menu-item index="1" @click="routerUrl('/questionBank')">
            <i class="el-icon-menu"></i>
            <span slot="title">题库管理</span>
          </el-menu-item>
          <el-menu-item index="2" @click="routerUrl('/setting')">
            <i class="el-icon-setting"></i>
            <span slot="title">活动管理</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="21" class="question-list-content">
        <el-row>
          <el-col :span="4"  class="que-content-title"><h3>活动管理</h3> </el-col>
          <el-col :span="3" :offset="17" class="user">{{username}}</el-col>
        </el-row>
        <el-row class="create-que-bank">
          <el-col :span="6" :offset="18">
            <el-button type="primary" class="primary-botton" v-if="HavCode"><a class="downCode" :href="imgUrl" :download="imgUrl">下载二维码</a> </el-button>
            <el-button type="primary" @click="dialogVisible = true" v-if="HavCode">编辑</el-button>
            </el-col>
        </el-row>
        <div class="setting-content">
          <el-form :model="formParam" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="主持人口令">
              <el-input v-model="formParam.adminCode" disabled="true" ></el-input>
            </el-form-item>
            <el-form-item label="活动链接">
              <el-input v-model="formParam.htmlUrl" disabled="true"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
    <canvas id="canvas" style="display: none"></canvas>
    <el-dialog
      title="更改口令"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <span><el-input v-model="formParam.adminCode" placeholder="请输入内容" ></el-input></span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="closeDialog()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="tiptool"
      width="30%"
      :before-close="handleClose">
      <span>获取数据失败，是否重试？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="tiptool = false">取 消</el-button>
        <el-button type="primary" @click="closeTip()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import QRCode from 'qrcode'
import utils from '@/assets/utils'
export default {
  data () {
    return {
      username: '',
      imgUrl: '',
      dialogVisible: false,
      HavCode: true,
      tiptool: false,
      formParam: {
        adminCode: '12',
        htmlUrl: 'www.baidu.com'
      }
      
    }
  },
  created () {
    if (sessionStorage.getItem('username')) {
      this.username = sessionStorage.getItem('username')
      this.getquebank()
    } else {
      this.$router.push({path:'/login'})
    }
  },
  methods: {
    routerUrl (url) {
      this.$router.push({path: url})
    },
    getquebank () {
      var self = this
      this.$ajax({
        method: 'get',
        url: '/get_setting_info/' + this.username, //后端给出URL
      }).then(resp => {
        self.formParam.adminCode = resp.adminCode
        //resp.url 为伪代码，具体格式根据后端而定
        self.formParam.htmlUrl = resp.url
        var canvas = document.getElementById('canvas')
        QRCode.toCanvas(canvas, resp,url, function (error) {
        if (error) {
          console.log(error)
        } else {
          console.log('success')
        }
        this.HavCode = true
        this.tiptool = false
      })
      this.imgUrl = canvas.toDataURL("image/png")
      }).catch(e => {
        this.HavCode = false
        this.tiptool = true
      })
    },
    closeTip () {
      this.tiptool = false
      setTimeout(() => {
        this.getquebank()
      }, 1000)
    },
    closeDialog () {
      var self = this
      if (utils.trim(self.formParam.adminCode).length > 0) {
        self.$ajax({
          method: 'put',
          url: '/config',
          data: {adminCode: self.formParam.adminCode}
        }).then(resp => {
          self.dialogVisible = false
          this.$message({
            message: '恭喜您，修改口令成功',
            type: 'success'
          });
          self.getquebank()
        }).catch(e => {
          this.$message.error('抱歉，修改口令失败，请重试');
        })
      }
      
    }
  }
}
</script>


<style>
.tac >.el-col-3 {
  border-right: solid 1px #e6e6e6;
  height: 100vh;
}
.tac >.el-col-3 > ul {
  border: none;
}
.que-content-title {
  text-align: left;
}
.que-content-title h3 {
  margin: 0;
}
.question-list-content {
  padding: 20px;
  height: 100vh;
  overflow: auto;
}
.create-que-bank {
  text-align: right;
  margin-top: 10px;
}
.user {
  text-align: right;
}
#canvas{
  width:200px!important;
  height:200px!important;
}
.downCode {
  display: block;
  color: #fff!important;
  padding: 12px 20px
}
.primary-botton {
  padding: 0;
}
.setting-content {
  width: 40%;
  margin: 100px auto
}
</style>

