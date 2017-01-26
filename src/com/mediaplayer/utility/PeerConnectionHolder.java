package com.mediaplayer.utility;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
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
    AudioTrack audioTrack;
    MediaConstraints audioConstraints;
    PeerConnection pc;
    Peer peer;
    DataChannel dataChannel;
    DataChannel.Init dc;
    private static final String AUDIO_TRACK_ID = "TRACK_ID_W";
    private static final String DATA_CHANNEL = "DATA_CHANNEL";
    private static final String AUDIO_CODEC_PARAM_BITRATE = "maxaveragebitrate";
    private static final String AUDIO_ECHO_CANCELLATION_CONSTRAINT = "googEchoCancellation";
    private static final String AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT = "googAutoGainControl";
    private static final String AUDIO_HIGH_PASS_FILTER_CONSTRAINT = "googHighpassFilter";
    private static final String AUDIO_NOISE_SUPPRESSION_CONSTRAINT = "googNoiseSuppression";
    private static final String AUDIO_LEVEL_CONTROL_CONSTRAINT = "levelControl";



    public PeerConnectionHolder(Peer observer, List<PeerConnection.IceServer> iceServers, DataChannel.Init dc) {
        peerConnectionFactory = new PeerConnectionFactory();
        audioConstraints = new MediaConstraints();
        peer = observer;
        this.dc = dc;
        dc.id = 1;

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        //createAudioConstraints();
        audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
        mediaStream = peerConnectionFactory.createLocalMediaStream("SOMEID");
        audioTrack =  peerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
        audioTrack.setEnabled(true);
        pc = peerConnectionFactory.createPeerConnection(iceServers, new MediaConstraints(), observer);

        //dataChannel = pc.createDataChannel(DATA_CHANNEL, dc);
        //dataChannel.registerObserver(peer);
        mediaStream.addTrack(audioTrack);
        this.peer = observer;

    }

    public PeerConnection getPc() {
        return pc;
    }


    public MediaStream getMediaStream() {
        return mediaStream;
    }

    public void createOffer() {
        pc.addStream(mediaStream);
        pc.createOffer(peer,audioConstraints);
    }

    public void createAnswer(String seesionType, String description) {
        SessionDescription sdp = new SessionDescription(SessionDescription.Type.fromCanonicalForm(seesionType), description);
        pc.setRemoteDescription(peer, sdp);
        pc.createAnswer(peer,audioConstraints);
    }

    public void setRemoteDescription(String seesionType, String description) {
        SessionDescription sdp = new SessionDescription(SessionDescription.Type.fromCanonicalForm(seesionType), description);
        pc.setRemoteDescription(peer,sdp);
    }

    private void createAudioConstraints(){
        /*audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_ECHO_CANCELLATION_CONSTRAINT, "false"));
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT, "false"));
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_HIGH_PASS_FILTER_CONSTRAINT, "false"));
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_NOISE_SUPPRESSION_CONSTRAINT, "false"));*/
    }
}
