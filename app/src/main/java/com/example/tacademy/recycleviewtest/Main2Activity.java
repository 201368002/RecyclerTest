package com.example.tacademy.recycleviewtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    AutoCompleteTextView msg_input;

    // 컬렉션 준비
    ArrayList<String> arrayList = new ArrayList<String>();

    String data[] = {
            "1 API 키 이 프로젝트  이 키를 지원하는 모든 API에서 사용할 수 있는 API 키입니다. 애플리케이션에서 이 키를 사용하려면 키를 key=API_KEY 매개변수로 전달하세요",
            "2 Android 앱의 사용량 제한 (선택사항)Android 앱의 사용량을 제한하려면 패키지 이름과 SHA-1 서명 인증서 지문을 추가하세요.AndroidManifest.xml 파일에서 패키지 이름을 가져온 후 다음 명령어를 사용하여 지문을 가져오세요.",
            "3 02-02 11:38:55.650 564-564/? V/DPM: |COMMON:COM| DpmCom::removeComEventHandler fd 42 02-02 11:38:55.650 564-564/? V/DPM: |COMMON:COM| waiting on events (-1ms)",
            "4 Google API가 마음에 드시나요? Google 인프라도 확인해 보세요. 무료 평가판을 신청하면 60일 동안 사용할 수 있는 크레딧 300달러가 제공되어 Google Cloud Platform을 살펴볼 수 있습니다. 자세히 알아보기",
            "5 * a View is two levels deep(wrt to ViewHolder.itemView). DisplayList can be invalidated by* setting View's visibility to INVISIBLE whenView is detached. On Kitkat and JB MR2, Recycler* recursively traverses itemView and invalidates display list for each ViewGroup that matches* this criteria.",
    };

    // 페이크
    int index[] = new int[1000];

    MyAdapter myAdapter = new MyAdapter();

    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(String s : data){
            arrayList.add(s);
        }

        // 콤퍼넌트 리소스를 자바 객체로 연결
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        msg_input = (AutoCompleteTextView)findViewById(R.id.msg_input);

        // ===========================================================
        // 자동완성 기능 추가
        msg_input.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                autoKeyword
                ));

        // ============================================================
        // 뷰의 스타일 (매니저) 정의, 선형, 그리드형, 높이가 불규칙한 그리드형
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 그리드
        gridLayoutManager = new GridLayoutManager(this, 2);
        //recyclerView.setLayoutManager(gridLayoutManager);

        // 가변 그리드
        staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        //recyclerView.setLayoutManager(staggeredGridLayoutManager);
        // ============================================================
        // 채팅관련
        // 데이터를 뒤집어서 표현할 때(최신순), 서버에서 온 데이터가 최신순이면 필요없음
        // FIREBASE로 데이터를 받아올때는 최신순 처리가 곤란하므로 유용함
        //staggeredGridLayoutManager.setReverseLayout(true);
        // 마지막 데이터가 보이게 (staggeredGridLayoutManager 미지원)
        // 데이터가 동적으로 바뀌면 적용이 안됨. 새로 세팅해야함.
        linearLayoutManager.setStackFromEnd(true);
        //gridLayoutManager.setStackFromEnd(true);
        // 특정 위치로 맞추기 ( 마지막 위치로 )
        linearLayoutManager.scrollToPosition(data.length-1);
        //staggeredGridLayoutManager.scrollToPosition(data.length-1);
        // ============================================================
        // 데이터 공급원 아답터 연결
        recyclerView.setAdapter(myAdapter);
    }

    // 전송 버튼 누르면
    public void onSend(View view){
        // 1. 입력데이터 추출
        String msg = msg_input.getText().toString();
        // 2. 서버 전송 -> 여기서는 데이터 직접 추가
        arrayList.add(msg);
        // 페이크
        index[arrayList.size()-1]=1;
        // 3. 화면 갱신
        myAdapter.notifyDataSetChanged();
        // 4. 리스트 가장 마지막으로 갱신
        //staggeredGridLayoutManager.scrollToPosition(arrayList.size()-1);
        linearLayoutManager.scrollToPosition(arrayList.size()-1);
        // 5. 입력값 제거
        msg_input.setText("");
        // 6. 키보드 내리기
        closeKeyword(this, msg_input);
    }

    // onSend 6. 키보드 내리기
    public void closeKeyword(Context context, EditText editText){
        InputMethodManager methodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    // =================================================================
    // 아답터
    class MyAdapter extends RecyclerView.Adapter{

        // 데이터의 개수
        @Override
        public int getItemCount() {
            return arrayList.size(); //data.length;
        }

        // ViewHolder 생성
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // xml -> view
            View itemView =
                LayoutInflater.from(parent.getContext())
                        //.inflate(R.layout.cell_cardview_layout, parent, false);
                        .inflate(R.layout.sendbird_view_group_user_message, parent, false);

            return new PostHolder(itemView);
        }

        // ViewHolder에 데이터를 설정 ( 바인딩 ) 한다.
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // 보이고자 하는 셀에 내용을 설정한다.
                                                                    // 페이크 -> index[position]
            ((PostHolder)holder).bindOnPost(arrayList.get(position), index[position]); //data[position]);
        }

        // 전에 사용한 getView = onCreateViewHolder + onBindViewHolder
    }

    // 컬렉션 키워드 -> 추천 키워드, 자동완성 UI를 직접 구성하거나, 내용을 가변시킬 수 있다.
    class MyKeywordAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }

    String autoKeyword[] =
        {
            "이상해씨",
                    "이상해풀",
                    "이상해꽃",
                    "파이리",
                    "리자드",
                    "리자몽",
                    "꼬부기",
                    "어니부기",
                    "거북왕",
                    "캐터피",
                    "단데기",
                    "버터플",
                    "뿔충이",
                    "딱충이",
                    "독침붕",
                    "구구",
                    "피죤",
                    "피죤투",
                    "꼬렛",
                    "레트라",
                    "깨비참",
                    "깨비드릴조",
                    "아보",
                    "아보크",
                    "피카츄",
                    "라이츄",
                    "모래두지",
                    "고지",
                    "니드런♀",
                    "니드리나",
                    "니드퀸",
                    "니드런♂",
                    "니드리노",
                    "니드킹",
                    "삐삐",
                    "픽시",
                    "식스테일",
                    "나인테일",
                    "푸린",
                    "푸크린",
                    "주뱃",
                    "골뱃",
                    "뚜벅쵸",
                    "냄새꼬",
                    "라플레시아",
                    "파라스",
                    "파라섹트",
                    "콘팡",
                    "도나리",
                    "디그다",
                    "닥트리오",
                    "나옹",
                    "페르시온",
                    "고라파덕",
                    "골덕",
                    "망키",
                    "성원숭",
                    "가디",
                    "윈디",
                    "발챙이",
                    "슈륙챙이[16]",
                    "강챙이",
                    "캐이시",
                    "윤겔라",
                    "후딘",
                    "알통몬",
                    "근육몬",
                    "괴력몬",
                    "모다피",
                    "우츠동",
                    "우츠보트",
                    "왕눈해",
                    "독파리",
                    "꼬마돌",
                    "데구리",
                    "딱구리",
                    "포니타",
                    "날쌩마",
                    "야돈",
                    "야도란",
                    "코일",
                    "레어코일",
                    "파오리",
                    "두두",
                    "두트리오",
                    "쥬쥬",
                    "쥬레곤",
                    "질퍽이",
                    "질뻐기",
                    "셀러",
                    "파르셀",
                    "고오스",
                    "고우스트",
                    "팬텀",
                    "롱스톤",
                    "슬리프",
                    "슬리퍼",
                    "크랩",
                    "킹크랩",
                    "찌리리공",
                    "붐볼",
                    "아라리",
                    "나시",
                    "탕구리",
                    "텅구리",
                    "시라소몬",
                    "홍수몬",
                    "내루미",
                    "또가스",
                    "또도가스",
                    "뿔카노",
                    "코뿌리",
                    "럭키",
                    "덩쿠리",
                    "캥카",
                    "쏘드라",
                    "시드라",
                    "콘치",
                    "왕콘치",
                    "별가사리",
                    "아쿠스타",
                    "마임맨",
                    "스라크",
                    "루주라",
                    "에레브",
                    "마그마",
                    "쁘사이저",
                    "켄타로스",
                    "잉어킹",
                    "갸라도스",
                    "라프라스",
                    "메타몽",
                    "이브이",
                    "샤미드",
                    "쥬피썬더",
                    "부스터",
                    "폴리곤",
                    "암나이트",
                    "암스타",
                    "투구",
                    "투구푸스",
                    "프테라",
                    "잠만보",
                    "프리져",
                    "썬더",
                    "파이어",
                    "미뇽",
                    "신뇽",
                    "망나뇽",
                    "뮤츠",
                    "뮤",
        };
}
