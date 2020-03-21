package com.example.battleships;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;


public class CAdapter extends BaseAdapter implements View.OnDragListener {

    private static final boolean PREVIEW_SHIP_MODE = true;

    Game game;
    Context context;
    CAdapter(Context context, Game game){
        this.game = game;
        this.context = context;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int getCount() {
        return 64;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View cell;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cell = inflater.inflate(R.layout.cell_item, parent, false);
        final ImageButton btn = cell.findViewById(R.id.cellBtn);

         switch (game.getCellStatusById(position)){
            case UNCOVERED:
            case BUSY:
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                if(PREVIEW_SHIP_MODE && game.getShipById(position) != null)
                    btn.setBackgroundColor(context.getResources().getColor(R.color.colorPreview));
                break;
            case HIT:
                btn.setForeground(context.getResources().getDrawable(R.drawable.ic_check_black_24dp));
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorCheck));
                break;
            case MISS:
                btn.setForeground(context.getResources().getDrawable(R.drawable.ic_x_black_24dp));
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorX));
                break;
            case DROWNED:
                btn.setForeground(context.getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
                btn.setBackgroundColor(context.getResources().getColor(R.color.colorDrowned));
                break;
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.updateCellStatusOnClicked(position);
                notifyDataSetChanged();
            }
        });

        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startDragAndDrop(null, new View.DragShadowBuilder(v), null, 0);
                return false;
            }
        });

        return cell;
    }

    @Override
    public boolean onDrag(View v, DragEvent event){
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                Toast.makeText(context, "STARTED", Toast.LENGTH_SHORT).show();
                return true;
            case DragEvent.ACTION_DROP:
                Toast.makeText(context, "DROP", Toast.LENGTH_SHORT).show();
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Toast.makeText(context, "END", Toast.LENGTH_SHORT).show();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                Toast.makeText(context, "LOC CHANGED", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;

        }
    }

}
