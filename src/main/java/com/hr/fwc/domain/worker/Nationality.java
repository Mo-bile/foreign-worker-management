package com.hr.fwc.domain.worker;

import java.util.Set;

public enum Nationality {
    VIETNAM("VN", "베트남", false),
    PHILIPPINES("PH", "필리핀", false),
    INDONESIA("ID", "인도네시아", false),
    CAMBODIA("KH", "캄볇ィ아", false),
    MYANMAR("MM", "미얀마", false),
    NEPAL("NP", "네팔", false),
    BANGLADESH("BD", "방글라데시", false),
    PAKISTAN("PK", "파키스탄", false),
    THAILAND("TH", "태국", false),
    SRI_LANKA("LK", "스리랑카", false),
    MONGOLIA("MN", "몽골", false),
    KAZAKHSTAN("KZ", "카자흐스탄", false),
    KYRGYZSTAN("KG", "키르기스스탄", false),
    UZBEKISTAN("UZ", "우즈베키스탄", false),
    TAJIKISTAN("TJ", "타지키스탄", false),
    CHINA("CN", "중국", false),
    USA("US", "미국", true),
    CANADA("CA", "캐나다", true),
    AUSTRALIA("AU", "호주", true),
    NEW_ZEALAND("NZ", "뉴질랜드", true),
    JAPAN("JP", "일본", false),
    UK("GB", "영국", true),
    GERMANY("DE", "독일", true),
    FRANCE("FR", "프랑스", true);

    private final String code;
    private final String koreanName;
    private final boolean socialSecurityAgreement;

    Nationality(String code, String koreanName, boolean socialSecurityAgreement) {
        this.code = code;
        this.koreanName = koreanName;
        this.socialSecurityAgreement = socialSecurityAgreement;
    }

    public String code() {
        return code;
    }

    public String koreanName() {
        return koreanName;
    }

    public boolean hasSocialSecurityAgreement() {
        return socialSecurityAgreement;
    }

    private static final Set<Nationality> SSA_COUNTRIES = Set.of(
        USA, CANADA, AUSTRALIA, NEW_ZEALAND, UK, GERMANY, FRANCE
    );

    public static boolean isSSACountry(Nationality nationality) {
        return SSA_COUNTRIES.contains(nationality);
    }

}
