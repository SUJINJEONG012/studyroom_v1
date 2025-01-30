/**
 * 
 */
document.addEventListener("DOMContentLoaded", function () {
      	const token = localStorage.getItem("token");
          const loginBtn = document.getElementById("login");
          const logoutBtn = document.getElementById("logout");
          const myPageBtn = document.getElementById("mypage");

          if (!token) {
              //alert("🚨 로그인이 필요합니다.");
              loginBtn.style.display = "inline-block"; // 로그인 버튼 보이기
              logoutBtn.style.display = "none"; // 로그아웃 숨기기
              myPageBtn.style.display = "none"; // 마이페이지 숨기기
              return;
          }
		  
		  if (logoutBtn) {
		          logoutBtn.addEventListener("click", function (event) {
		              event.preventDefault();
		              localStorage.removeItem("token"); // JWT 삭제
		              sessionStorage.removeItem("token"); 
		              window.location.href = "/login"; // 로그인 페이지로 이동
		          });
		      }

          fetch("/api/user", {
              method: "GET",
              headers: {
                  "Authorization": "Bearer " + token
              }
          })
          .then(response => response.json()) // json 변환
          .then(data => {
              if (data.uid) {
                  console.log(`✅ 로그인됨: ${data.uid}`);
                  document.getElementById("uid").innerText = `반갑습니다 ${data.uid}님 `; // 사용자 이름 표시

                  loginBtn.style.display = "none"; // 로그인 버튼 숨기기
                  logoutBtn.style.display = "inline-block"; // 로그아웃 버튼 보이기
                  myPageBtn.style.display = "inline-block"; // 마이페이지 버튼 보이기
              } else {
                  console.log("🚨 사용자 정보 없음, 로그인 필요");
                  loginBtn.style.display = "inline-block";
                  logoutBtn.style.display = "none";
                  myPageBtn.style.display = "none";
              }
          })
          .catch(error => {
              console.error("사용자 정보 가져오기 실패", error);
              loginBtn.style.display = "inline-block";
              logoutBtn.style.display = "none";
              myPageBtn.style.display = "none";
          });
      });