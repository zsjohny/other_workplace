package com.qianmi.open.api.qmcs.channel;

import com.qianmi.open.api.qmcs.ResetableTimer;

import java.net.URI;
import java.util.Hashtable;

public class ClientChannelSharedSelector implements ClientChannelSelector {
	private final static int CONNECT_TIMEOUT = 5000;
	private Hashtable<String, ClientChannel> channels;
	private Object lockObject;
	private int heartbeatInterval;

	public ClientChannelSharedSelector() {
		this.channels = new Hashtable<String, ClientChannel>();
		this.lockObject = new Object();
	}

	public void setHeartbeat(int interval) {
		this.heartbeatInterval = interval;
	}

	public ClientChannel getChannel(URI uri) throws ChannelException {
		final String url = uri.toString();
		if (channels.get(url) == null ||
				!channels.get(url).isConnected()) {
			synchronized (this.lockObject) {
				if (channels.get(url) == null ||
						!channels.get(url).isConnected()) {
					channels.put(url, this.wrapChannel(
							this.connect(uri, CONNECT_TIMEOUT)));
				}
			}
		}
		return channels.get(url);
	}

	public void returnChannel(ClientChannel channel) {
		// shared channel
	}

	protected ClientChannel connect(URI uri, int timeout) throws ChannelException {
		if (uri.getScheme().equalsIgnoreCase("ws") ||
				uri.getScheme().equalsIgnoreCase("wss")) {
			return EmbeddedWebSocketClient.connect(uri, timeout);
		}
		throw new ChannelException("not supported protocol");
	}

	private ClientChannel wrapChannel(final ClientChannel channel) {
		if (this.heartbeatInterval > 0)
			channel.setHeartbeatTimer(new ResetableTimer(this.heartbeatInterval));
		return channel;
	}
}
