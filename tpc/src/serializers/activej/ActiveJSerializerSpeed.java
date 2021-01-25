package serializers.activej;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import data.media.MediaTransformer;
import io.activej.serializer.BinarySerializer;
import io.activej.serializer.CompatibilityLevel;
import io.activej.serializer.SerializerBuilder;
import io.activej.serializer.StringFormat;
import io.activej.serializer.annotations.Serialize;
import io.activej.serializer.annotations.SerializeNullable;
import io.activej.serializer.annotations.SerializeStringFormat;
import serializers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActiveJSerializerSpeed {

    public static void register(TestGroups groups) {
        register(groups.media, ActiveJSerializerSpeed.mediaTransformer);
    }

    private static void register(TestGroup group, Transformer transformer) {
        group.add(transformer, new DefaultSerializer(),
                new SerFeatures(SerFormat.BINARY,
                        SerGraph.FLAT_TREE,
                        SerClass.CLASSES_KNOWN,
                        "code generation"));
    }

    public static class DefaultSerializer extends Serializer<DMediaContent> {
        private BinarySerializer<DMediaContent> serializer;
        private byte[] array = new byte[300];

        public DefaultSerializer() {
            serializer = SerializerBuilder.create(ClassLoader.getSystemClassLoader())
                    .withCompatibilityLevel(CompatibilityLevel.LEVEL_3)
                    .build(DMediaContent.class);
        }

        @Override
        public DMediaContent deserialize(byte[] array) throws Exception {
            return serializer.decode(array, 0);
        }

        @Override
        public byte[] serialize(DMediaContent content) throws Exception {
            int len = serializer.encode(array, 0, content);
            return Arrays.copyOf(array, len);
        }

        @Override
        public String getName() {
            return "activej-speed";
        }
    }

    public static class DImage {
        public enum Size {
            SMALL, LARGE
        }

        @Serialize(order = 1)
        @SerializeStringFormat(StringFormat.ISO_8859_1)
        public String uri;

        @Serialize(order = 2)
        @SerializeStringFormat(StringFormat.UTF8)
        @SerializeNullable
        public String title; // can be null;

        @Serialize(order = 3)
        public int width;

        @Serialize(order = 4)
        public int height;

        @Serialize(order = 5)
        public Size size;

        public DImage(String uri, String title, int width, int height, Size size) {
            this.uri = uri;
            this.title = title;
            this.width = width;
            this.height = height;
            this.size = size;
        }

        public DImage() {}
    }

    public static class DMedia {
        public enum Player {
            JAVA, FLASH
        }

        @Serialize(order = 1)
        @SerializeStringFormat(StringFormat.ISO_8859_1)
        public String uri;

        @Serialize(order = 2)
        @SerializeNullable
        @SerializeStringFormat(StringFormat.UTF8)
        public String title; // can be null

        @Serialize(order = 3)
        public int width;

        @Serialize(order = 4)
        public int height;

        @Serialize(order = 5)
        @SerializeStringFormat(StringFormat.ISO_8859_1)
        public String format;

        @Serialize(order = 6)
        public long duration;

        @Serialize(order = 7)
        public long size;

        @Serialize(order = 8)
        public int bitrate; // can be unset

        @Serialize(order = 9)
        public boolean hasBitrate;

        @Serialize(order = 10)
        @SerializeStringFormat(value = StringFormat.UTF8, path = 0)
        public List<String> persons;

        @Serialize(order = 11)
        public Player player;

        @Serialize(order = 12)
        @SerializeNullable
        @SerializeStringFormat(StringFormat.UTF8)
        public String copyright; // can be null

        public DMedia() {}
    }

    public static class DMediaContent {
        @Serialize(order = 1)
        public List<DImage> DImages;

        @Serialize(order = 2)
        public DMedia DMedia;

        public DMediaContent(List<DImage> list, DMedia DMedia) {
            this.DImages = list;
            this.DMedia = DMedia;
        }

        public DMediaContent() {}
    }

    public static MediaTransformer<DMediaContent> mediaTransformer = new MediaTransformer<DMediaContent>() {
        @Override
        public DMediaContent forward(MediaContent a) {
            return new DMediaContent(forwardImages(a.getImages()), forwardMedia(a.getMedia()));
        }

        @Override
        public MediaContent reverse(DMediaContent a) {
            return new MediaContent(reverseMedia(a.DMedia), reverseImages(a.DImages));
        }

        @Override
        public MediaContent shallowReverse(DMediaContent a) {
            return new MediaContent(reverseMedia(a.DMedia), Collections.<Image>emptyList());
        }
    };

    private static List<DImage> forwardImages(List<Image> images) {
        List<DImage> list = new ArrayList<>(images.size());
        for (Image image : images) {
            list.add(forwardImage(image));
        }
        return list;
    }

    private static DMedia forwardMedia(Media m) {
        DMedia dMedia = new DMedia();
        dMedia.uri = m.uri;
        dMedia.title = m.title;
        dMedia.duration = m.duration;
        dMedia.format = m.format;
        dMedia.persons = m.persons;
        dMedia.bitrate = m.bitrate;
        dMedia.hasBitrate = m.hasBitrate;
        dMedia.copyright = m.copyright;
        dMedia.width = m.width;
        dMedia.height = m.height;
        dMedia.player = forwardPlayer(m.player);
        dMedia.size = m.size;
        return dMedia;
    }

    private static DImage forwardImage(Image i) {
        DImage dImage = new DImage();
        dImage.uri = i.uri;
        dImage.title = i.title;
        dImage.width = i.width;
        dImage.height = i.height;
        dImage.size = forwardSize(i.size);
        return dImage;
    }

    private static List<Image> reverseImages(List<DImage> DImages) {
        List<Image> list = new ArrayList<>(DImages.size());
        for (DImage DImage : DImages) {
            list.add(reverseImage(DImage));
        }
        return list;
    }

    private static Media reverseMedia(DMedia m) {
        return new Media(m.uri, m.title, m.width, m.height,
                m.format, m.duration, m.size, m.bitrate,
                m.hasBitrate, m.persons, reversePlayer(m.player),
                m.copyright);
    }

    private static Image reverseImage(DImage i) {
        return new Image(i.uri, i.title, i.width, i.height, reverseSize(i.size));
    }

    private static Image.Size reverseSize(DImage.Size oldSize) {
        return Image.Size.values()[(oldSize.ordinal())];
    }

    private static Media.Player reversePlayer(DMedia.Player oldPlayer) {
        return Media.Player.values()[(oldPlayer.ordinal())];
    }

    private static DImage.Size forwardSize(Image.Size oldSize) {
        return DImage.Size.valueOf(oldSize.name());
    }

    private static DMedia.Player forwardPlayer(Media.Player oldPlayer) {
        return DMedia.Player.valueOf(oldPlayer.name());
    }
}
