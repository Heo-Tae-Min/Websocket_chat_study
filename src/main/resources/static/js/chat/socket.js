'use strict';

// document.write("<script src='jquery-3.6.1.js'></script>")
document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")

var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

// roomId 파라미터 가져오기
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');

function connect(event) {
    username = document.querySelector('#name').value.trim();

    // 연결하고자하는 Socket 의 endPoint
    var socket = new SockJS('/ws-stomp');
    
    stompClient = Stomp.over(socket);
    
    // {}는 header에 담길 내용, 뒤의 함수들은 콜백 함수
    stompClient.connect({}, onConnected, onError);

    event.preventDefault();
}

function onConnected() {
    // sub 할 url => /sub/chat/room/roomId 로 구독한다
    stompClient.subscribe('/sub/chat/room/' + roomId, onMessageReceived);

    // 서버에 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보냄
    stompClient.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            "userId": userId,
            type: 'ENTER'
        })
    )
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// 메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

// 메시지를 받을 때도 마찬가지로 JSON 타입으로 받으며,
// 넘어온 JSON 형식의 메시지를 parse 해서 사용한다.
function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);

    var messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(chat.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(chat.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(chat.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(chat.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)