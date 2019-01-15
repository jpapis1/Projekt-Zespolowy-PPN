

var app3 = new Vue({
    delimiters: ['[[', ']]'],
    el: '#app-3',
    data: {
        message: jsonObject[0],
        beg: 0,
        end: 23,
        range: [0,1,2,3,4,5,6,7,8,9,10,11,12,13
                ,14,15,16,17,18,19,20,21,22,23]
    },
    methods: {
    getData: function (i) {
      
      this.message = jsonObject[i]
    },

    addRange: function() {
        // jsonObject.length
        if( this.end <= 8725-24){
            this.beg += 24
            this.end += 24
            var list = [];
            for (var i = this.beg; i <= this.end; i++) {
                list.push(i);
            }
            this.range = list
        }

    },
    removeRange: function() {
        if(this.beg != 0){
            this.beg -= 24
            this.end -= 24
            var list = [];
            for (var i = this.beg; i <= this.end; i++) {
                list.push(i);
            }
            this.range = list
        }

    }
  }
})