export default {
  loading: function (container) {
    // var loading = container.querySelector('.data-loading')
    // if (!loading) {
    //   loading = document.createElement('div')
    //   loading.className = 'data-loading'
    //   container.appendChild(loading)
    //   loading.dataset.count = 0
    // }
    // loading.dataset.count = +loading.dataset.count + 1
    // if ((!container.style.position || container.style.position === 'static') && !container.classList.contains('data-loading-relative')) {
    //   container.classList.add('data-loading-relative')
    // }
  },
  hideLoading: function (container) {
    var loading = container.querySelector('.data-loading')
    if (loading) {
      var count = +loading.dataset.count
      if (count > 1) {
        loading.dataset.count = count - 1
      } else {
        container.classList.remove('data-loading-relative')
        container.removeChild(loading)
      }
    }
  },
  getNowFormatDate: function () {
    var date = new Date()
    var seperator1 = '-'
    var seperator2 = ':'
    var month = date.getMonth() + 1
    var strDate = date.getDate()
    if (month >= 1 && month <= 9) {
      month = '0' + month
    }
    if (strDate >= 0 && strDate <= 9) {
      strDate = '0' + strDate
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + ' ' + date.getHours() + seperator2 + date.getMinutes() + seperator2 + date.getSeconds()
    return currentdate
  },
  getNowFormatDates: function () {
    var date = new Date()
    var seperator1 = '-'
    var seperator2 = ':'
    var month = date.getMonth() + 1
    var strDate = date.getDate()
    if (month >= 1 && month <= 9) {
      month = '0' + month
    }
    if (strDate >= 0 && strDate <= 9) {
      strDate = '0' + strDate
    }
    var currentdates = date.getFullYear() + seperator1 + month + seperator1 + strDate 
    return currentdates
  },
  getFrag: function (key) {
    var args = Array.prototype.slice.apply(arguments);
    args.unshift(location.hash);
    var getQueryString = function (queryStr) {
        var len = arguments.length;
        if (len === 1) {
            var querys = {};
            queryStr.replace(/(?:\?|&)([^=]+)=([^&$]*)/g, function (all, key, val) {
                querys[key] = decodeURIComponent(val);
            });
            return querys;
        } else if (len === 2) {
            var rst = new RegExp('[?&]' + arguments[1] + '=([^&$]*)').exec(queryStr);
            return rst && decodeURIComponent(rst[1]);
        }
    };
    return getQueryString.apply(null, args);
  },
  trim: function (str) {
    return str.replace(/^\s*|\s*$/g, '');
  },

  dateIntoTimeStamp: function (str) {
    
    var timeStamp = str.replace(/\-/g,'/');
    var date = Date.parse(new Date(timeStamp));
    console.log(date)
    return date.getTime();
  },

  timeStampIntoDate: function (strs) {
    var dataTime = new Date(parseInt(strs)).toLocaleString();
    var dataArr = dataTime.split('/');
    if (dataArr[1].length == 1) {
      dataArr[1] = '0' + dataArr[1]
    }
    if (this.trim(dataArr[2]).length == 1) {
      dataArr[2] = '0' + dataArr[2]
    }

    return dataArr[0] + '-' + dataArr[1] + '-' + this.trim(dataArr[2]);
  }

}
