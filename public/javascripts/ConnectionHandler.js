import './node_modules/jquery/dist/jquery.js'

export class ConnectionHandler
{
    socket;
    message_stack = [];
    updateFunction;
    chatMessageHandler;

    constructor(updateFunction, chatMessageHandler) {
        this.updateFunction = updateFunction
        this.chatMessageHandler = chatMessageHandler
    }

    sendMove(playerID, move, fen) {
        this.socket.send(JSON.stringify({"type": "move", "PlayerID": playerID, "UCI": move, "FEN": fen}))
    }

    async requestPlayerId() {
        const result = await $.get("/online_multiplayer/new_game")
        document.cookie = "PlayerID=" + result.PlayerID + "; path=/";
        document.cookie = "GameID=" + result.GameID + "; path=/";
        document.cookie = "color=w" + "; path=/";
        return {
            playerId: result.PlayerID,
            gameId: result.GameID,
            color: 'w'
        };
    }

    onmessage(message) {
        switch (message.type){
            case "UCI":
                this.updateFunction(message.UCI)
                break
            case "message":
                this.message_stack.push({player: "opponent", message: message.message})
                this.chatMessageHandler()
                break
            case "timeout":
                break
        }
    }

    async connectToWebSocket(playerID) {
        this.socket = new WebSocket("ws://localhost:9000/websocket");

        try {
            await this.waitForSocketOpen(this.socket);

            this.socket.send(JSON.stringify({type: "register", "PlayerID": playerID}));
            this.socket.onmessage = (event) => {
                const data = JSON.parse(event.data);
                this.onmessage(data)
            };

            setInterval(() => {
                if (this.socket.readyState === WebSocket.OPEN) {
                    this.socket.send(JSON.stringify({ type: 'timeout' }));
                }
            }, 70000);

        } catch (error) {
        }
    }

    async requestGameSession(){
        const playerID = this.getCookie("PlayerID")
        return await $.post("online_multiplayer/join_game", {PlayerID: playerID})
    }


    getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null
    }

    sendMessage(playerID, message) {
        this.socket.send(JSON.stringify({"type": "message", "PlayerID": playerID, "message": message}))
        this.message_stack.push({player: "you", message: message})
        this.chatMessageHandler()
    }

    waitForSocketOpen(socket, timeout = 5000) {
        return new Promise((resolve, reject) => {
            const timer = setTimeout(() => {
                reject(new Error('WebSocket opening timeout exceeded'));
            }, timeout);

            socket.onopen = () => {
                clearTimeout(timer);
                resolve();
            };

            socket.onerror = (err) => {
                clearTimeout(timer);
                reject(err);
            };
        });
    }
}
