//shared vars to avoid polluting namespace
var sharedVars = {
		saveRoutineTimeout: null
}

//update navigation bar asynchronously
var updateNav = function() {
	AJAXGet("/updatenav", function(responseText) {
		var mainElm = document.getElementById("main");
		var oldNav = document.getElementById("navigation");
		
		var div = document.createElement("div");
		div.innerHTML = responseText;
		var newNav = div.firstChild
		
		mainElm.replaceChild(newNav, oldNav); //remove current nav
	});
}

var showElementWithId = function(idName) {
			var element = document.getElementById(idName);
			element.style.display = "inline-block";
}

var hideElementWithId = function(idName) {
		var element = document.getElementById(idName);
		elements.style.display = "none";
}

var showElements = function(className) {
	var elements = document.getElementsByClassName(className);
	for(var i = 0; i < elements.length; i++) {
		elements[i].style.display = "inline-block";
	}
}

var hideElements = function(className) {
	var elements = document.getElementsByClassName(className);
	for(var i = 0; i < elements.length; i++) {
		elements[i].style.display = "none";
	}
}
   
var roundToFive = function(val) {
   	return Math.round(val / 5) * 5;
}
    
var resetWeeks = function() {
	switchWeek(document.getElementById("week-1-select"));
}

var switchWeek = function(elm) {
  	var weekElms = document.getElementsByClassName("week-select");
	for(var i = 0; i < weekElms.length; i++) {
		weekElms[i].classList.remove("active");
	}
	elm.classList.add("active");
	var weekNumber = elm.dataset["weekNumber"];
	document.getElementById("current-week-input").value = weekNumber;
	var repElements = document.getElementsByClassName("current-reps");
	for(var i = 0; i < repElements.length; i++) {
		var updatedReps = 7 + parseInt(weekNumber);
		if(repElements[i].tagName == "INPUT") {
			repElements[i].value = updatedReps;
		} else {
			repElements[i].textContent = updatedReps;
		}
	}
	
	var messageElm = document.getElementById("message-alert");

	if(weekNumber !== "1" && weekNumber !== "5") {
		messageElm.textContent = "The weight stays the same from week-to-week," +
				" but the reps go up. The weight increases after each cycle.";
	} else if(weekNumber === "5") {
		messageElm.textContent = "Week 5 is test week! On Day A, go all out" +
				" and try to get 12 reps. If you can, great! If not, hold the" +
				" weight steady for that exercise in the next Cycle. After this" +
				" week, go onto the next Cycle.";
	} else {
		messageElm.textContent = "This is your recommended routine. Save your " +
				"routine as you progress through weeks and cycles and we will " +
				"track where you're at. If necessary, you can customize the " +
				"starting weight by adjusting the sliders.";
	}
}

var displaySliderNumbers = function(elementId, className, dataElementName) {
	var sliderElement = document.getElementById(elementId);
	var currentElements = document.getElementsByClassName(className);
	for(var i = 0; i < currentElements.length; i++) {
		if (currentElements[i].dataset[dataElementName] == elementId) {
			if (currentElements[i].parentNode.id.indexOf("day-b") > -1) {
				currentElements[i].textContent = roundToFive(sliderElement.value * 0.9);
			} else if (currentElements[i].parentNode.id.indexOf("day-c") > -1) {
				currentElements[i].textContent = roundToFive(sliderElement.value * 0.8);
			} else {
				currentElements[i].textContent = sliderElement.value;
			}
		}
	}			
}

/* Replace content of the element whose id is the second parameter with the
 * html code defined in the first parameter. This function is useful if we
 * have html that is in text form rather than a js element.
 */
var replaceContent = function(html, elementId) {
	var element = document.getElementById(elementId);
	var div = document.createElement("div");
	div.innerHTML = html;
	var outerElement = div.firstElementChild;
	element.innerHTML = outerElement.innerHTML;
}

var getCycleNumber = function() {
	var elm = document.getElementById("current-cycle-number")
	return elm.textContent.trim();
}

var updateCycleNumber = function(nextOrLast) {
	var curCycle = getCycleNumber();
	var nextCycle = nextOrLast == "last" ? parseInt(curCycle) - 1 : parseInt(curCycle) + 1;
	document.getElementById("current-cycle-number").textContent = nextCycle;
}

var renderNextCycle = function(element) {
	renderCycle(element, "/routine/nextcycle/" + (parseInt(getCycleNumber()) + 1));
    updateCycleNumber("next");    
}

var renderPreviousCycle = function(element) {
	if(getCycleNumber() != "1") {
		renderCycle(element, "/routine/previouscycle/" + (parseInt(getCycleNumber()) - 1));
	    updateCycleNumber("last");
	}
}

var renderCycle = function(element, url) {
	var request = new XMLHttpRequest();
	request.open("GET", url, true);

	request.onload = function() {
	  if (request.status >= 200 && request.status < 400) {
	    var data = request.responseText;
	    replaceContent(data, "initial-routine");
	    resetWeeks();
	  } else {
	  }
	};

	request.onerror = function() {
	  // There was a connection error of some sort
	};
	request.send();
}

var saveRoutine = function(elm) {
	if(sharedVars.saveRoutineTimeout !== null) {
		clearTimeout(sharedVars.saveRoutineTimeout); //in case this function has been called with a timeout
	}
	var formElm = document.getElementById("routine-form");
	if(elm.dataset["alsoCreateAccount"] == "true") {
		createAccount();
	} else {
		AJAXSubmit(formElm, handleResponse);
	}
}

var saveAfterCreate = function(request) {
	replaceContent(request.responseText, "new-user-article");
	if(request.responseText.indexOf("error-message") > 0) {
		console.log("errors are present");
	} else { //no errors
		var saveButtonElm = document.getElementById("save-button");
		saveButtonElm.textContent = "Save";
	 	saveButtonElm.dataset["alsoCreateAccount"] = false;
	 	saveRoutine(saveButtonElm);
	 	console.log("Response: " + request.responseText);
	}
}

var createAccount = function() {
	var newUserForm = document.getElementById("new-user-form");
 	AJAXSubmit(newUserForm, saveAfterCreate);			 
}

var handleResponse = function(request) {
	 if(request.status >= 200 && request.status <= 400) {
		 console.log("request was successful. status: " + request.status);
		 var elm = document.getElementById("save-status");
		 elm.innerHTML = request.responseText;
		 var saveButtonElm = document.getElementById("save-button");
		 if(document.getElementById("new-user-form")) { //user has no account
		 	saveButtonElm.textContent = "Create account + Save";
		 	saveButtonElm.dataset["alsoCreateAccount"] = true;
		 } else {
			 var saveMessage = document.getElementById("save-success");
			 var timer = setTimeout(function() {
				 saveMessage.parentNode.removeChild(saveMessage);
			 }, 3000)
			 updateNav();
		 }
	 } else {
		 console.log("request failed. status: " + request.status);
	 }
}

var requestRandomUsernamePassword = function(elm) {
	AJAXGet("/joinus/random", function(responseText) {
		 var createOwnUsernameElm = document.getElementById("save-status");
		 createOwnUsernameElm.innerHTML = ""; //clear area to enter own username/pass
		 var elm = document.getElementById("save-area-content");
		 elm.innerHTML = responseText; //place response from server in this element
		 var saveButtonElm = document.getElementById("save-button"); 
		 saveButtonElm.textContent = "Save"; //change save button text
		 saveButtonElm.dataset["alsoCreateAccount"] = false;
		 sharedVars.saveRoutineTimeout = setTimeout(saveRoutine, 1000, document.getElementById("save-button"));
	});
}

