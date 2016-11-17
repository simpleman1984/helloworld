// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// The function below is executed in the context of the inspected page.
var page_getProperties = function() {
  var data = window.jQuery && $0 ? jQuery.data($0) : {};
  // Make a shallow copy with a null prototype, so that sidebar does not
  // expose prototype.
  var props = Object.getOwnPropertyNames(data);
  var copy = { __proto__: null };
  for (var i = 0; i < props.length; ++i)
    copy[props[i]] = data[props[i]];
  return copy;
}
chrome.devtools.panels.elements.createSidebarPane(
    "jQuery1 Properties",
    function(sidebar) {
  function updateElementProperties() {
    sidebar.setExpression("(" + page_getProperties.toString() + ")()");
  }
  updateElementProperties();
  chrome.devtools.panels.elements.onSelectionChanged.addListener(
      updateElementProperties);
});


var console_ = function(msg){
    chrome.devtools.inspectedWindow.eval(
          'console.log(unescape("' + escape(msg) + '"))');
};

console_(1);
chrome.devtools.panels.create("Font Picker",
                              "bd.png",
                              "panel.html",
                              function(panel) {
        
});
//https://developer.chrome.com/extensions/devtools_network#type-Request
//https://developer.chrome.com/extensions/devtools_panels
console.info(3);
console_(2);


chrome.devtools.network.onRequestFinished.addListener(
  function(request) {
    request.getContent(function(c){
        if(c.indexOf("29:8013") != -1){
            console_("-----------------finded url:"+request.request.url);
        }
    });
    if (request.response.bodySize > 1024*1024) {
        
      chrome.devtools.inspectedWindow.eval(
          'console.log("Large image: " + unescape("' +
          escape(request.request.url) + '"))');
    }
});