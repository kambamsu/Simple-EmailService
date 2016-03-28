function checkemail() {
	var str = document.getElementById("fromEmail").value;
	var filter = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
	if (filter.test(str.trim())) {
		var toEmails = document.getElementById("toEmail").value.split(",");
		alert(toEmails)
		for (var i=0; i < toEmails.length; i++) {
			if (!filter.test(toEmails[i].trim())) {
				return false;
			}
		}
		var ccEmails = document.getElementById("ccEmail");
		if (ccEmails != null) {
			var ccEmailArr = ccEmails.split(",");
			for (var i=0; i < ccEmailArr.length; i++) {
				if (!filter.test(ccEmailArr[i].trim())) {
					return false;
				}
			}
		}

		if (document.getEleemntById("emailSubject").value == "") {
			alert("Sending email with empty subject");
		}
		
		if (document.getEleemntById("emailBody").value == "") {
			alert("Sending email with empty body");
		}
		return true;
	} else {
		return false;
	}
	return true;
}
