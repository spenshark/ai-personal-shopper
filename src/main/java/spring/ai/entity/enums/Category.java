package spring.ai.entity.enums;

import lombok.Getter;

@Getter
public enum Category {

    // 아우터
    OUTER("아우터", null),
    JACKET("자켓", OUTER),
    COAT("코트", OUTER),
    PADDING("패딩", OUTER),
    CARDIGAN("가디건", OUTER),

    // 상의
    TOP("상의", null),
    TSHIRT("티셔츠", TOP),
    SHIRT("셔츠", TOP),
    BLOUSE("블라우스", TOP),
    KNIT("니트", TOP),

    // 하의
    PANTS("바지", null),
    SLACKS("슬랙스", PANTS),
    JEANS("청바지", PANTS),

    // 스커트
    SKIRT("스커트", null),
    MINI_SKIRT("미니스커트", SKIRT),
    LONG_SKIRT("롱스커트", SKIRT),

    // 원피스
    DRESS("원피스", null),

    // 신발
    SHOES("신발", null),

    // 가방
    BAG("가방", null),

    // 액세서리
    ACC("악세사리", null);

    private final String description;
    private final Category parent;

    Category(String description, Category parent) {
        this.description = description;
        this.parent = parent;
    }

    public boolean isSubCategoryOf(Category parent) {
        if (this.parent == null) {
            return false;
        }
        return this.parent.equals(parent);
    }
}
