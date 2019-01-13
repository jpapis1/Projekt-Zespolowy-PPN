

var app3 = new Vue({
    delimiters: ['[[', ']]'],
    el: '#app-3',
    data: {
        message: jsonObject[0],
        beg: 0,
        end: 20,
        range: [0,1,2,3,4,5,6,7,8,9,10,11,12,13
                ,14,15,16,17,18,19,20]
    },
    methods: {
    getData: function (i) {
      
      this.message = jsonObject[i]
    },

    addRange: function() {
        // jsonObject.length
        if( this.end <= 8725-20){
            this.beg += 20
            this.end += 20
            var list = [];
            for (var i = this.beg; i <= this.end; i++) {
                list.push(i);
            }
            this.range = list
        }

    },
    removeRange: function() {
        if(this.beg != 0){
            this.beg -= 20
            this.end -= 20
            var list = [];
            for (var i = this.beg; i <= this.end; i++) {
                list.push(i);
            }
            this.range = list
        }

    }
  }
})