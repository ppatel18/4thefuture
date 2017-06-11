var coursera = (function(){
  var link = "https://api.coursera.org/api/courses.v1?"
  
  return {
    search: function(searchString){
      qp = {
        q:"search",
        query:searchString,
        primaryLanguages:"en",
        fields:['photoUrl','domainTypes','categories']
      }
      var xhr = new XMLHttpRequest()
      
      var curl = new URLSearchParams()
      for(var pair of Object.entries(qp)){
        curl.append(pair[0], pair[1])
      }
      
      xhr.open('GET',link+curl.toString(),false)
      xhr.send(null)
      
      if (xhr.readyState == 4 && xhr.status == 200) {
        var response = JSON.parse(xhr.responseText)
        return response.elements
      }
      return null
    }
  }
})();

// coursera.search("machine learning")

// [
//     {
//       "courseType": "v2.ondemand",
//       "domainTypes": [
//         {
//           "subdomainId": "machine-learning",
//           "domainId": "data-science"
//         },
//         {
//           "subdomainId": "data-analysis",
//           "domainId": "data-science"
//         }
//       ],
//       "photoUrl": "https://d3njjcbhbojbot.cloudfront.net/api/utilities/v1/imageproxy/https://coursera-course-photos.s3.amazonaws.com/a1/e7472069b611e3ae92c39913bb30e0/PredictionMachineLearning.jpg",
//       "categories": [
//         "infotech",
//         "stats"
//       ],
//       "id": "H02KsW1DEeWXrA6ju0fvnQ",
//       "slug": "practical-machine-learning",
//       "name": "Practical Machine Learning"
//     },
//     {
//       "courseType": "v2.ondemand",
//       "domainTypes": [
//         {
//           "subdomainId": "machine-learning",
//           "domainId": "data-science"
//         },
//         {
//           "subdomainId": "data-analysis",
//           "domainId": "data-science"
//         }
//       ],
//       "photoUrl": "https://d3njjcbhbojbot.cloudfront.net/api/utilities/v1/imageproxy/https://coursera.s3.amazonaws.com/topics/neuralnets/large-icon.png",
//       "categories": [
//         "stats",
//         "cs-ai"
//       ],
//       "id": "meQ0ic9uEeWu4RLrx6VBYw",
//       "slug": "neural-networks",
//       "name": "Neural Networks for Machine Learning"
//     },
//     {
//       "courseType": "v2.ondemand",
//       "domainTypes": [
//         {
//           "subdomainId": "data-analysis",
//           "domainId": "data-science"
//         },
//         {
//           "domainId": "business",
//           "subdomainId": "leadership-and-management"
//         }
//       ],
//       "photoUrl": "https://d3njjcbhbojbot.cloudfront.net/api/utilities/v1/imageproxy/https://coursera-course-photos.s3.amazonaws.com/f8/d9a0901e1411e6b4be05fc1f155449/python_datascience_thumbnail_machinelearning_1x1.png",
//       "categories": [],
//       "id": "di4l_R0lEeaP7xL2JHHq4w",
//       "slug": "python-machine-learning",
//       "name": "Applied Machine Learning in Python"
//     }
// ]