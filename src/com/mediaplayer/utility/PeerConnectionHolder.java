package com.mediaplayer.utility;

import org.webrtc.AudioSource;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.List;

/**
 * Created by shrikanth on 1/22/17.
 */

public class PeerConnectionHolder {
    private MediaStream mediaStream = null;
    PeerConnectionFactory peerConnectionFactory;
    AudioSource audioSource;
    MediaConstraints audioConstraints;
    PeerConnection pc;
    Peer peer;


    public PeerConnectionHolder(Peer observer, List<PeerConnection.IceServer> iceServers) {
        peerConnectionFactory = new PeerConnectionFactory();
        audioConstraints = new MediaConstraints();
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        // First we create an AudioSource
        audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
        mediaStream = peerConnectionFactory.createLocalMediaStream("SOMEID");
        pc = peerConnectionFactory.createPeerConnection(iceServers, audioConstraints, observer);
        this.peer = observer;

    }

    public PeerConnection getPc() {
        return pc;
    }


    public MediaStream getMediaStream() {
        return mediaStream;
    }

    public void createOffer() {
        pc.createOffer(peer,audioConstraints);
    }

    public void createAnswer(String seesionType, String description) {
        SessionDescription sdp = new SessionDescription(SessionDescription.Type.fromCanonicalForm(seesionType), description);
        pc.setRemoteDescription(peer, sdp);
        pc.createAnswer(peer,audioConstraints);
    }
}
