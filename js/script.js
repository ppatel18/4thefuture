//Speech to text

 var recognition = new webkitSpeechRecognition();
         recognition.continuous = true;
         recognition.interimResults = true;
         recognition.onresult = function(event) { 
            //console.log(event); 
            var r = event.results[event.results.length - 1][0].transcript;
            var geometry = ['octahedron', 'sphere', 'cylinder', 'dodecahedron'];
            //console.log(r);
            for(var i=0; i < geometry.length; i++) {
                var res = r.toLowerCase().match(geometry[i]); //matches strings inside array
                //final_span.innerHTML = res[0];
                //console.log(res);
                if(res != null) {
                var speech_answer = document.getElementById('speech_answer');
                //final_span.innerHTML = event.results [0][event.results.length - 1].transcript;        
                speech_answer.innerHTML = res[0];
                var p = document.getElementById(res[0] + "_wrapper");
                var correct = document.getElementById(res[0]);
                p.setAttribute("color", "green");
                correct.setAttribute("color", "green");
                //setTimeout(function () {
                //correct.setAttribute("color", "yellow");
                //}, 500);
                }
            }
         };
         recognition.start();
             /* var recognition = new webkitSpeechRecognition();
              * recognition.continuous = true;
              * recognition.interimResults = true;
              *     /* recognition.onstart = function() { ... }
              *      * recognition.onresult = function(event) { ... }
              *      * recognition.onerror = function(event) { ... }
              *      * recognition.onend = function() { ... }*/
              
// enable mic on mobile  
/*global navigator*/
    //navigator.webkitGetUserMedia({ audio: true, video: false });
    
    //navigator.webkitGetUserMedia({video:audio}, onSuccess, onFail);          
              
// A-FRAME

// Counting Game