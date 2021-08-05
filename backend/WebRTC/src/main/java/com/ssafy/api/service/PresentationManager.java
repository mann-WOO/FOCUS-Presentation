package com.ssafy.api.service;


import com.ssafy.common.util.Presentation;
import com.ssafy.common.util.Room;
import com.ssafy.common.util.UserSession;
import com.sun.xml.bind.v2.TODO;
import org.apache.catalina.Pipeline;
import org.kurento.client.ImageOverlayFilter;
import org.kurento.client.MediaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PresentationManager {
    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    /*
    key: [방 이름]-[발표자 이름]
    value: 발표자료, pipeline , 등등
     */
    private final ConcurrentMap<String, Presentation> presentations = new ConcurrentHashMap<>();

    //TODO 임시자료 -> 나중에는 db에서 경로 가져와서 Presentation.class - presentationImageUris 에 저장되어야함
    private String[] imageUris={
            "/home/ubuntu/presentations/demo/bird.jpg",
            "/home/ubuntu/presentations/demo/cat.jpg",
            "/home/ubuntu/presentations/demo/cat2.jpg",
            "/home/ubuntu/presentations/demo/flower.jpg",
            "/home/ubuntu/presentations/demo/pets.jpg"
    };
    private int imageIndex;
    private Presentation presentation;
    private ImageOverlayFilter imageOverlayFilter;
    private UserSession presenter;

    //ImageOverlayFilter 위치, 크기 설정 변수
    private float offsetXPercent = 0.05f;
    private float offsetYPercent = 0.25f;
    private float widthPrecent = 0.64f;
    private float heightPrecent = 0.36f;
    private boolean keepAspectRatio = false;
    private boolean imageCenter = true;
    private boolean isFullScreen = false;


    public Presentation getPresentation(String presenterName, Room room, UserSession presenter){
        String key=room.getName()+"-"+presenterName;
        presentation=presentations.get(key);

        MediaPipeline pipeline=room.getPipeline();
        if(presentation==null){
            presentation=new Presentation(presenterName, imageUris, room.getName(), pipeline);
            presentations.put(key, presentation);
            this.presenter=presenter;

        }
        return presentation;
    }

    public void setPresenter() {
        presenter.setPresenter();
    }

    public void start(){
        imageIndex=0;
        String imageId = "presentation" +imageIndex;
        String imageUri = imageUris[imageIndex];
        imageOverlayFilter=new ImageOverlayFilter.Builder(presentation.getPipeline()).build();
        imageOverlayFilter.addImage(imageId, imageUri, offsetXPercent, offsetYPercent, widthPrecent, heightPrecent, keepAspectRatio, imageCenter);
        log.info("[start] imageId: {}, imageUri: {}", imageId, imageUri);

        presenter.getOutgoingWebRtcPeer().connect(imageOverlayFilter);
        imageOverlayFilter.connect(presenter.getIncomingMedia(presenter.getName()));
    }

    public void prev(String sdpOffer) {
        if(imageIndex > 0) {
            String removeImageId = "testImage" + imageIndex;

            imageOverlayFilter=new ImageOverlayFilter.Builder(presentation.getPipeline()).build();


            imageIndex--;
            String addImageId = "testImage" + imageIndex;
            String addImageUri = imageUris[imageIndex];
            imageOverlayFilter.addImage(addImageId, addImageUri, offsetXPercent, offsetYPercent, widthPrecent, heightPrecent, keepAspectRatio, imageCenter);
            imageOverlayFilter.removeImage(removeImageId);
            presenter.getOutgoingWebRtcPeer().connect(imageOverlayFilter);
            imageOverlayFilter.connect(presenter.getIncomingMedia(presenter.getName()));
            //presenter.linkImageOverlayPipeline(presenter, imageOverlayFilter);

            /*
            TODO 다른 사람 remote 영상에 발표자의 자료를 보여주기 위해서 파이프라인 연결을 다시 해야하는데
                 negotiation 중복 현상 발생함
                 심지어 이미지 적용도 안됨
             */
//            try{
//                presenter.receiveVideoFrom(presenter, sdpOffer);
//            }catch (Exception e){
//                e.printStackTrace();
//                log.error("error: {}, sdpOffer: {}", e.getMessage(), sdpOffer);
//            }

        }else{
            log.info("[prev] 맨 처음 사진입니다.");
        }
    }


    public void next(String sdpOffer){
        if(imageIndex < imageUris.length-1) {
            String removeImageId = "testImage" + imageIndex;

            imageOverlayFilter=new ImageOverlayFilter.Builder(presentation.getPipeline()).build();


            imageIndex++;
            String addImageId = "testImage" + imageIndex;
            String addImageUri = imageUris[imageIndex];
            imageOverlayFilter.addImage(addImageId, addImageUri, offsetXPercent, offsetYPercent, widthPrecent, heightPrecent, keepAspectRatio, imageCenter);
            imageOverlayFilter.removeImage(removeImageId);
            presenter.getOutgoingWebRtcPeer().connect(imageOverlayFilter);
            imageOverlayFilter.connect(presenter.getIncomingMedia(presenter.getName()));

            /*
            TODO prev() 내용과 동일일
            */
            //presenter.linkImageOverlayPipeline(presenter, imageOverlayFilter);
//            try{
//                presenter.receiveVideoFrom(presenter, sdpOffer);
//            }catch (Exception e){
//                e.printStackTrace();
//                log.error("error: {}, sdpOffer: {}", e.getMessage(), sdpOffer);
//            }

        }else{
            log.info("[next] 마지막 사진입니다.");
        }
    }


    public void full() {
        if(isFullScreen){
            offsetXPercent = 0.05f;
            offsetYPercent = 0.25f;
            widthPrecent = 0.65f;
            heightPrecent = 0.45f;
            isFullScreen = false;
        }else{
            offsetXPercent = 0.0f;
            offsetYPercent = 0.0f;
            widthPrecent = 1.0f;
            heightPrecent = 1.0f;
            isFullScreen = true;
        }

        log.info("[full toggle] isFullScreen: {}", isFullScreen);

        String imageId = "testImage" + imageIndex;
        String imageUri = imageUris[imageIndex];
        imageOverlayFilter.removeImage(imageId);
        imageOverlayFilter.addImage(imageId, imageUri, offsetXPercent, offsetYPercent, widthPrecent, heightPrecent, keepAspectRatio, imageCenter);
        presenter.getOutgoingWebRtcPeer().connect(imageOverlayFilter);
        imageOverlayFilter.connect(presenter.getIncomingMedia(presenter.getName()));
    }
}