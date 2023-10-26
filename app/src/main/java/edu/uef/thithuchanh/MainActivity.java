package edu.uef.thithuchanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uef.thithuchanh.databasse.DatabaseHelper;
import edu.uef.thithuchanh.model.FoodItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView selectedItemTextView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodList;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        selectedItemTextView = findViewById(R.id.selectedItemTextView);
        databaseHelper = new DatabaseHelper(this);

        databaseHelper.clearSelectedFoods();

        // Khởi tạo danh sách món ăn
        foodList = new ArrayList<>();
        foodList.add(new FoodItem("Bread", R.drawable.bread));
        foodList.add(new FoodItem("Cherry Cheesecake", R.drawable.cherrycheesecake));
        foodList.add(new FoodItem("Gingerbread House", R.drawable.gingerbreadhouse));
        foodList.add(new FoodItem("Hamburger", R.drawable.hamburger));
        foodList.add(new FoodItem("Sunny Side Up Eggs", R.drawable.sunnysideupeggs));

        foodList.add(new FoodItem("Beer", R.drawable.beer));
        foodList.add(new FoodItem("Coconut Cocktail", R.drawable.coconutcocktail));
        foodList.add(new FoodItem("Lemonade", R.drawable.lemonade));
        foodList.add(new FoodItem("Milkshake", R.drawable.milkshake));
        foodList.add(new FoodItem("Orange Juice", R.drawable.orangejuice));


        List<FoodItem> firstFiveFoods = foodList.subList(0, 5);

        List<FoodItem> lastFiveFoods = foodList.subList(5, 10);
        Collections.reverse(lastFiveFoods);

        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                FoodItem selectedFood = foodList.get(position);
                String selectedFoodName = selectedFood.getName();
                selectedItemTextView.setText("Selected item: " + selectedFoodName);
                Toast.makeText(MainActivity.this, "You selected: " + selectedFood.getName(), Toast.LENGTH_SHORT).show();

                // Lưu trữ món ăn đã chọn vào cơ sở dữ liệu
                databaseHelper.addSelectedFood(selectedFoodName);

                // Hiển thị danh sách các món ăn đã chọn trong TextView
                List<String> selectedFoodsList = databaseHelper.getAllSelectedFoods();
                StringBuilder selectedFoodsText = new StringBuilder();
                for (String food : selectedFoodsList) {
                    selectedFoodsText.append(food).append("\n");
                }
                selectedItemTextView.setText(selectedFoodsText.toString());
            }
        });
    }

    private static class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
        private List<FoodItem> foodList;
        private OnItemClickListener onItemClickListener;

        public FoodAdapter(List<FoodItem> foodList) {
            this.foodList = foodList;
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }

        @NonNull
        @Override
        public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
            return new FoodViewHolder(view, onItemClickListener);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
            FoodItem foodItem = foodList.get(position);
            holder.foodImageView.setImageResource(foodItem.getImageId());
            holder.foodNameTextView.setText(foodItem.getName());
        }

        @Override
        public int getItemCount() {
            return foodList.size();
        }

        public static class FoodViewHolder extends RecyclerView.ViewHolder {
            ImageView foodImageView;
            TextView foodNameTextView;

            public FoodViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                foodImageView = itemView.findViewById(R.id.foodImageView);
                foodNameTextView = itemView.findViewById(R.id.foodNameTextView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }
    }
}