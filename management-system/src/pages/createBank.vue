<template>
  <div class="create-bank">
    <el-row class="tac">
      <el-col :span="3">
        <h3><router-link to="/">小木科技</router-link></h3>
        <el-menu
          default-active="1"
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
      <el-col :span="21" class="create-content">
        <el-row>
          <el-col :span="4" class="que-content-title"><h3>{{pageTitle}}</h3> </el-col>
          <el-col :span="3" :offset="17" class="user">{{username}}</el-col>
        </el-row>
        <el-row class="create-que-bank">
          <el-col :span="6" :offset="18">
            <el-button type="primary" @click="saveQuesBank()" :loading="isLoading">保存</el-button>
            <el-button plain @click="goreturn()">返回</el-button>
            </el-col>
        </el-row>
        <div class="create-ques-content">

          <div class="create-ques-form">
            <el-form ref="form" label-width="100px" class="question-title">
              <el-form-item label="题库名称">
                <el-input v-model="questionName" placeholder="请输入题库名称"></el-input>
              </el-form-item>
            </el-form>
            <el-form :model="questionRule" status-icon :rules="rules2" ref="questionRule" label-width="100px" class="demo-ruleForm">
              <el-form-item label="问题" prop="quest">
                <el-input v-model="questionRule.quest" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="选项1" prop="ans1">
                <el-input v-model="questionRule.ans1" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="选项2" prop="ans2">
                <el-input v-model.number="questionRule.ans2"></el-input>
              </el-form-item>
              <el-form-item label="选项3" prop="ans3">
                <el-input v-model.number="questionRule.ans3"></el-input>
              </el-form-item>
              <el-form-item label="正确选项">
                <el-radio-group v-model="ansradio">
                  <el-radio :label="0">选项1</el-radio>
                  <el-radio :label="1">选项2</el-radio>
                  <el-radio :label="2">选项3</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="submitForm('questionRule')">{{buttonTitle}}</el-button>
                <el-button @click="resetForm('questionRule')">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div class="quest-content-list" >
            <el-card class="box-card" v-for="(item, index) in questions" :key="item">
              <div slot="header" class="clearfix">
                <span class="ques-card-title">问题：{{ item.title }} <br> 正确选项：{{item.resultIndex[0] === 0 ? '选项1' : (item.resultIndex[0] === 1 ? '选项2' : '选项3')}}</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="removeArr(index)">删除</el-button>
              </div>
              <div class="text item">
                选项1：{{ item.options[0]}}
              </div>
              <div class="text item">
                选项2：{{ item.options[1]}}
              </div>
              <div class="text item">
                选项3：{{ item.options[2]}}
              </div>
            </el-card>
          </div>
          
        </div>
      </el-col>
      
    </el-row>
    <el-dialog
    title="提示" :visible.sync="dialogVisible" width="30%">
      <span>抱歉，获取题目列表失败，是否重试？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDelog(0)">取 消</el-button>
        <el-button type="primary" @click="closeDelog(1)">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import utils from '@/assets/utils'
export default {
  data () {
    var checkQuest = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('问题不能为空'))
      } else {
        callback()
      }
    }
    var validateAns = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('选项不能为空'))
      } else {
        callback()
      }
    }
    return {
      username: '',
      pageTitle: '',
      buttonTitle: '',
      isLoading: false,
      dialogVisible: false,
      ansradio: 0,
      questions: [],
      questionName: '',
      questionRule: {
        quest: '',
        ans1: '',
        ans2: '',
        ans3: ''
      },
      rules2: {
        ans3: [
          { validator: validateAns, trigger: 'blur' }
        ],
        ans2: [
          { validator: validateAns, trigger: 'blur' }
        ],
        ans1: [
          { validator: validateAns, trigger: 'blur' }
        ],
        quest: [
          { validator: checkQuest, trigger: 'blur' }
        ]
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
      if (JSON.stringify(utils.getFrag()) === '{}') {
        this.pageTitle = '创建题库'
        this.buttonTitle = '添加并新建'
      } else {
        var frag = utils.getFrag()
        this.pageTitle = '修改题库'
        this.buttonTitle = '更改题目'
        this.$ajax({
          method: 'get',
          url: '/question_bank/' + utils.getFrag().ques,
        }).then(resp => {
          this.dialogVisible = false
          this.questions = resp.questions
          this.questionRule = {
            quest: this.questions[frag.index].title,
            ans1: this.questions[frag.index].options[0],
            ans2: this.questions[frag.index].options[1],
            ans3: this.questions[frag.index].options[2]
          }
          this.ansradio = parseInt(this.questions[frag.index].resultIndex[0])
        }).catch(e => {
          this.dialogVisible = true
        })
      }
    },
    closeDelog (index) {
      this.dialogVisible = false
      if (index === 1) {
        setTimeout(() => {
          this.getquebank()
        }, 1000)
      }
    },
    goreturn () {
      this.$router.go(-1)
    },
    submitForm(formName) {
      var self = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (JSON.stringify(utils.getFrag()) !== '{}') {
            self.questions[utils.getFrag().index] = {
              title: self.questionRule.quest,
              options: [self.questionRule.ans1, self.questionRule.ans2, self.questionRule.ans3],
              resultIndex: [self.ansradio]
            }
          } else {
            self.questions.unshift({
              title: self.questionRule.quest,
              options: [self.questionRule.ans1, self.questionRule.ans2, self.questionRule.ans3],
              resultIndex: [self.ansradio]
            })
          }
          this.$refs[formName].resetFields()
          this.$notify({
            message: '本题已暂存，注意，刷新页面暂存题目将会删除',
            type: 'success'
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    removeArr (index) {
      this.questions.splice(index, 1)
      this.$notify({
        message: '删除成功',
        type: 'success'
      })
    },
    saveQuesBank () {
      var self = this
      if (!this.questionName) {
        this.$message.error('您还没给题库起名字哟~')
        return false
      }
      if (this.questions.length === 0) {
        this.$message.error('您的题库没有题呢，先添加些题目吧~')
        return false
      }
      self.isLoading = true

      if (JSON.stringify(utils.getFrag()) === '{}') {
        self.$ajax({
          method: 'post',
          url: '/question_bank/' + this.username,
          data: {
            name: self.questionName,
            questions: self.questions
          }
        }).then(resp => {
          this.$message({
            message: '恭喜您，成功创建题库',
            type: 'success'
          })
          self.isLoading = false
          self.questions = []
        }).catch(e => {
          this.$message.error('抱歉，创建题库不成功，请重试')
          self.isLoading = false
        })
      } else {
        self.$ajax({
          method: 'PUT',
          url: '/question_bank/' + utils.getFrag().ques,
          data: {
            name: self.questionName,
            questions: self.questions
          }
        }).then(resp => {
          this.$message({
            message: '恭喜您，成功修改题库',
            type: 'success'
          })
          self.isLoading = false
          self.questions = []
        }).catch(e => {
          this.$message.error('抱歉，修改题库不成功，请重试')
          self.isLoading = false
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
.create-content {
  padding: 20px;
  height: 100vh;
  overflow: auto;
}
.que-content-title {
  text-align: left;
}
.que-content-title h3 {
  margin: 0;
}
.create-que-bank {
  text-align: right;
  margin-top: 10px;
}
.create-ques-form {
  width: 40%;
  margin: auto;
  margin-top: 20px;
}
.user {
  text-align: right;
}
.question-title {
   border-bottom: solid 1px #e6e6e6;
   margin-bottom: 40px;
}
.text {
  font-size: 14px;
}
.item {
  margin-bottom: 18px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 30%;
  text-align: left;
  display: inline-block;
  margin: 10px;
}
.quest-content-list {
  text-align: left;
}
.ques-card-title {
  display: inline-block;
  width: 70%
}
</style>
